package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.dto.TradeRecordInsertDTO;
import gr.aueb.cf.pharmapp.exceptions.UserAnauthorizedException;
import gr.aueb.cf.pharmapp.model.Pharmacy;
import gr.aueb.cf.pharmapp.model.PharmacyContact;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.service.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Hibernate;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/record-trade")
public class RecordTradeController extends BaseServlet {
    private ITradeRecordService tradeService;
    private IPharmacyService pharmacyService;
    private IUserService userService;

    @Override
    public void init() {
        EntityManagerFactory emf = getEntityManagerFactory();
        this.tradeService = new TradeRecordServiceImpl(emf);
        this.pharmacyService = new PharmacyServiceImpl(emf);
        this.userService = new UserServiceImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User sessionUser = (User) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get fresh user data with initialized relationships
            User user = userService.getUserEntityByUsername(sessionUser.getUsername());

            String giverIdParam = request.getParameter("giverId");
            String receiverIdParam = request.getParameter("receiverId");

            if(receiverIdParam == null || receiverIdParam.isEmpty()) {
                throw new IllegalArgumentException("receiver pharmacy " +
                        "parameters");
            }

            if (giverIdParam == null || giverIdParam.isEmpty()) {
                throw new IllegalArgumentException("giver pharmacy parameters");
            }



            Long giverId = Long.parseLong(giverIdParam);
            Long receiverId = Long.parseLong(receiverIdParam);

            // Get pharmacies without full relationships since we just need IDs
            Pharmacy giver = pharmacyService.getPharmacyEntityById(giverId);
            Pharmacy receiver = pharmacyService.getPharmacyEntityById(receiverId);

            // Check access using ID comparison instead of object reference
            boolean isGiver = user.getPharmacies().stream()
                    .anyMatch(p -> p.getId().equals(giverId));

            boolean isReceiver = user.getPharmacies().stream()
                    .anyMatch(p -> p.getId().equals(receiverId));

            if (!isGiver && !isReceiver) {
                throw new UserAnauthorizedException("You don't have access to this pharmacy");
            }


            request.setAttribute("giverPharmacy", giver);
            request.setAttribute("receiverPharmacy", receiver);
            request.setAttribute("contactName", getContactName(user, receiver));
            request.setAttribute("fromContactName", getContactName(user, giver));
            request.getRequestDispatcher("/WEB-INF/jsp/record-trade.jsp").forward(request, response);

        }catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid pharmacy ID format");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
        catch (Exception e) {
            request.setAttribute("error", "Error loading trade form: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {

            if (request.getParameter("giverId") == null ||
                    request.getParameter("receiverId") == null ||
                    request.getParameter("amount") == null ||
                    request.getParameter("description") == null ||
                    request.getParameter("transactionDate") == null) {
                throw new IllegalArgumentException("Missing required parameters");
            }

            // Get form parameters
            Long giverId = Long.parseLong(request.getParameter("giverId"));
            Long receiverId = Long.parseLong(request.getParameter("receiverId"));
            double amount = Double.parseDouble(request.getParameter("amount"));
            String description = request.getParameter("description");
            LocalDateTime transactionDate = LocalDateTime.parse(request.getParameter("transactionDate"));

            // Create DTO and record trade
            TradeRecordInsertDTO dto = new TradeRecordInsertDTO();
            dto.setGiverPharmacyId(giverId);
            dto.setReceiverPharmacyId(receiverId);
            dto.setAmount(amount);
            dto.setDescription(description);
            dto.setTransactionDate(transactionDate);
            dto.setRecorderUserId(user.getId());

            tradeService.recordTrade(dto, user.getId());

            // Redirect with success message
            request.getSession().setAttribute("successMessage", "Trade recorded successfully!");
            response.sendRedirect(request.getContextPath() + "/pharmapp/dashboard?pharmacyId=" + giverId);

        }catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format in parameters");
            doGet(request, response);
        }
        catch (Exception e) {
            request.setAttribute("error", "Error recording trade: " + e.getMessage());
            doGet(request, response); // Redisplay the form with error
        }
    }

    private String getContactName(User user, Pharmacy pharmacy) {
        if (user == null || pharmacy == null) {
            return "Unknown";
        }

        // Simple null-safe check for contacts
        if (user.getContacts() != null) {
            for (PharmacyContact contact : user.getContacts()) {
                if (contact != null &&
                        contact.getPharmacy() != null &&
                        contact.getPharmacy().getId().equals(pharmacy.getId())) {
                    return contact.getContactName();
                }
            }
        }

        return pharmacy.getName(); // Default to pharmacy name if no contact found
    }
}