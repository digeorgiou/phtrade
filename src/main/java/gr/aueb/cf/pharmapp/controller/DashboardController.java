package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.controller.BaseServlet;
import gr.aueb.cf.pharmapp.dto.PharmacyBalanceDTO;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.service.ITradeRecordService;
import gr.aueb.cf.pharmapp.service.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pharmapp/dashboard")
public class DashboardController extends BaseServlet {
    private IUserService userService;
    private ITradeRecordService tradeService;

    @Override
    public void init() {
        // Initialize services
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get logged in user from session
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {

            // Refresh user data to ensure we have the latest pharmacies and
            // contacts
            user = userService.getUserEntityByUsername(user.getUsername());

            // Get selected pharmacy
            String pharmacyId = request.getParameter("pharmacyId");
            Pharmacy selectedPharmacy = null;
            List<PharmacyBalanceDTO> balanceList = new ArrayList<>();

            if (pharmacyId != null && !pharmacyId.isEmpty()) {
                selectedPharmacy = user.getPharmacies().stream()
                        .filter(p -> p.getId().equals(Long.parseLong(pharmacyId)))
                        .findFirst()
                        .orElse(null);

                if (selectedPharmacy != null) {
                    // Get balances with all contacts
                    for (PharmacyContact contact : user.getContacts()) {
                        double balance = tradeService.calculateBalanceBetweenPharmacies(
                                selectedPharmacy.getId(),
                                contact.getPharmacy().getId()
                        );

                        balanceList.add(new PharmacyBalanceDTO(
                                contact.getContactName(),
                                contact.getPharmacy().getName(),
                                contact.getPharmacy().getId(),
                                balance
                        ));
                    }
                }
            }

            request.setAttribute("user", user);
            request.setAttribute("selectedPharmacy", selectedPharmacy);
            request.setAttribute("balanceList", balanceList);
            request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}