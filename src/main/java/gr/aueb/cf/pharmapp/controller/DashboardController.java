package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.controller.BaseServlet;
import gr.aueb.cf.pharmapp.dto.PharmacyBalanceDTO;
import gr.aueb.cf.pharmapp.dto.RecentTradeDTO;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.service.ITradeRecordService;
import gr.aueb.cf.pharmapp.service.IUserService;
import gr.aueb.cf.pharmapp.service.TradeRecordServiceImpl;
import gr.aueb.cf.pharmapp.service.UserServiceImpl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Hibernate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/pharmapp/dashboard")
public class DashboardController extends BaseServlet {
    private IUserService userService;
    private ITradeRecordService tradeService;

    @Override
    public void init() {
        EntityManagerFactory emf = getEntityManagerFactory();
        this.userService = new UserServiceImpl(emf);
        this.tradeService = new TradeRecordServiceImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get logged in user from session
        User sessionUser = (User) request.getSession().getAttribute("user");

        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get fresh user data from database
            User user = userService.getUserEntityByUsername(sessionUser.getUsername());

            // Get selected pharmacy
            String pharmacyId = request.getParameter("pharmacyId");

            // Get search and sort parameters
            String searchTerm = request.getParameter("search");
            String sortBy = request.getParameter("sort");

            Pharmacy selectedPharmacy = null;
            List<PharmacyBalanceDTO> balanceList = new ArrayList<>();

            if (pharmacyId != null && !pharmacyId.isEmpty()) {
                selectedPharmacy = user.getPharmacies().stream()
                        .filter(p -> p.getId().equals(Long.parseLong(pharmacyId)))
                        .findFirst()
                        .orElse(null);

                if (selectedPharmacy != null) {


                    Hibernate.initialize(user.getContacts());

                    List<PharmacyContact> contacts = getFilteredSortedContacts(user, searchTerm, sortBy);

                    // Get balances with all contacts
                    for (PharmacyContact contact : contacts) {
                        Pharmacy contactPharmacy = contact.getPharmacy();
                        if (contactPharmacy == null) continue;  // Skip if no pharmacy

                        double balance = tradeService.calculateBalanceBetweenPharmacies(
                                selectedPharmacy.getId(),
                                contactPharmacy.getId()
                        );

                        Integer tradeCount =
                                tradeService.getTradeCountBetweenPharmacies(selectedPharmacy.getId(), contactPharmacy.getId());

                        List<RecentTradeDTO> recentTrades = tradeService.getRecentTradesBetweenPharmacies(
                                selectedPharmacy.getId(),
                                contactPharmacy.getId(),
                                5
                        );

                        if (recentTrades == null) {
                            recentTrades = new ArrayList<>();
                        }

                        // Ensure all fields have values
                        balanceList.add(new PharmacyBalanceDTO(
                                contact.getContactName() != null ? contact.getContactName() : "Contact",
                                contactPharmacy.getName() != null ? contactPharmacy.getName() : "Pharmacy",
                                contactPharmacy.getId(),
                                balance,
                                recentTrades != null ? recentTrades :
                                        new ArrayList<>(),
                                tradeCount

                        ));
                    }

                    if (sortBy != null) {
                        balanceList = sortBalanceList(balanceList, sortBy);
                    }
                }
            }

            request.setAttribute("user", user);
            request.setAttribute("selectedPharmacy", selectedPharmacy);
            request.setAttribute("balanceList", balanceList);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("currentSort", sortBy);
            request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    private List<PharmacyContact> getFilteredSortedContacts(User user, String searchTerm, String sortBy) {
        List<PharmacyContact> contacts = new ArrayList<>(user.getContacts());

        // Apply search filter
        if (searchTerm != null && !searchTerm.isEmpty()) {
            contacts = contacts.stream()
                    .filter(c -> c.getContactName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            c.getPharmacy().getName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Apply simple sorting
        if (sortBy != null) {
            switch (sortBy) {
                case "name":
                    contacts.sort(Comparator.comparing(PharmacyContact::getContactName));
                    break;
                case "name-desc":
                    contacts.sort(Comparator.comparing(PharmacyContact::getContactName).reversed());
                    break;
                // Trade count sorting will be handled after we get the balance list
            }
        }

        return contacts;
    }

    private List<PharmacyBalanceDTO> sortBalanceList(List<PharmacyBalanceDTO> balanceList, String sortBy) {
        switch (sortBy) {
            case "trades":
                balanceList.sort(Comparator.comparingInt(PharmacyBalanceDTO::getTradeCount).reversed());
                break;
            case "trades-desc":
                balanceList.sort(Comparator.comparingInt(PharmacyBalanceDTO::getTradeCount));
                break;
        }
        return balanceList;
    }

}