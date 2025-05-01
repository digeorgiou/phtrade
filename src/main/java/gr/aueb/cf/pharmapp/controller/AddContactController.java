package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.dto.BasePharmacyContactDTO;
import gr.aueb.cf.pharmapp.dto.PharmacyReadOnlyDTO;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.service.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add-contact")
public class AddContactController extends BaseServlet {
    private IPharmacyService pharmacyService;
    private IPharmacyContactService contactService;
    private IUserService userService;

    @Override
    public void init() {
        EntityManagerFactory emf = getEntityManagerFactory();
        this.pharmacyService = new PharmacyServiceImpl(emf);
        this.contactService = new PharmacyContactServiceImpl(emf);
        this.userService = new UserServiceImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nameSearch = request.getParameter("nameSearch");
        String userSearch = request.getParameter("userSearch");

        try {
            List<PharmacyReadOnlyDTO> searchResults = new ArrayList<>();

            if (nameSearch != null && !nameSearch.trim().isEmpty()) {
                searchResults = pharmacyService.searchPharmaciesByName(nameSearch.trim());
                request.setAttribute("searchType", "name");
            }
            else if (userSearch != null && !userSearch.trim().isEmpty()) {
                searchResults = pharmacyService.searchPharmaciesByUser(userSearch.trim());
                request.setAttribute("searchType", "user");
            }

            request.setAttribute("searchResults", searchResults);
            request.setAttribute("nameSearch", nameSearch);
            request.setAttribute("userSearch", userSearch);
            request.getRequestDispatcher("/WEB-INF/jsp/add-contact.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error searching pharmacies: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/add-contact.jsp").forward(request, response);
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

        String pharmacyId = request.getParameter("pharmacyId");
        String contactName = request.getParameter("contactName");

        try {
            if (pharmacyId == null || pharmacyId.isEmpty() || contactName == null || contactName.isEmpty()) {
                throw new IllegalArgumentException("Pharmacy and contact name are required");
            }

            BasePharmacyContactDTO contactDTO = new BasePharmacyContactDTO();
            contactDTO.setUserId(user.getId());
            contactDTO.setPharmacyId(Long.parseLong(pharmacyId));
            contactDTO.setContactName(contactName);

            contactService.saveContact(contactDTO);

            // Refresh user in session
            User updatedUser = userService.getUserEntityByUsername(user.getUsername());
            request.getSession().setAttribute("user", updatedUser);

            request.getSession().setAttribute("successMessage", "Contact added successfully!");
            response.sendRedirect(request.getContextPath() + "/pharmapp/dashboard");
        } catch (Exception e) {
            request.setAttribute("error", "Error adding contact: " + e.getMessage());
            doGet(request, response); // Redisplay the search form with error
        }
    }
}
