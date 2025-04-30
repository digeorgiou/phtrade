package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.dto.PharmacyInsertDTO;
import gr.aueb.cf.pharmapp.dto.PharmacyReadOnlyDTO;
import gr.aueb.cf.pharmapp.exceptions.PharmacyAlreadyExistsException;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.service.IPharmacyService;
import gr.aueb.cf.pharmapp.service.IUserService;
import gr.aueb.cf.pharmapp.service.PharmacyServiceImpl;
import gr.aueb.cf.pharmapp.service.UserServiceImpl;
import gr.aueb.cf.pharmapp.validator.PharmacyValidator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/add-pharmacy")
public class AddPharmacyController extends BaseServlet {
    private IPharmacyService pharmacyService;
    private IUserService userService;

    @Override
    public void init() {
        EntityManagerFactory emf = getEntityManagerFactory();
        pharmacyService = new PharmacyServiceImpl(emf);
        userService = new UserServiceImpl(emf);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/add-pharmacy.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Clear previous attributes
        request.removeAttribute("error");
        request.removeAttribute("nameMessage");

        // Data binding
        String name = request.getParameter("name");
        request.setAttribute("name", name);

        // Check for flash message
        String flashMessage = (String) request.getSession().getAttribute("flashMessage");
        if (flashMessage != null) {
            request.setAttribute("successMessage", flashMessage);
            request.getSession().removeAttribute("flashMessage"); // Clear after reading
        }

        try {
            // Create DTO
            PharmacyInsertDTO dto = new PharmacyInsertDTO(name);


            // Validate input
            PharmacyValidator validator = new PharmacyValidator(getEntityManagerFactory());
            Map<String, String> errors = validator.validate(dto);

            if (!errors.isEmpty()) {
                String nameMessage = errors.getOrDefault("name", "");
                request.setAttribute("nameMessage", nameMessage);
                request.getRequestDispatcher("/WEB-INF/jsp/add-pharmacy.jsp").forward(request, response);
                return;
            }

            // Add pharmacy
            PharmacyReadOnlyDTO pharmacy = pharmacyService.createPharmacy(dto, user.getId());

            User updatedUser =
                    userService.getUserEntityByUsername(user.getUsername());
            request.getSession().setAttribute("user", updatedUser);



            // Redirect to dashboard with success message
            request.getSession().setAttribute("flashMessage", "Pharmacy '" + pharmacy.getName() + "' added successfully!");
            response.sendRedirect(request.getContextPath() + "/pharmapp/dashboard");

        } catch (PharmacyAlreadyExistsException e) {
            request.setAttribute("error", "A pharmacy with this name already exists");
            request.getRequestDispatcher("/WEB-INF/jsp/add-pharmacy.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error adding pharmacy: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/add-pharmacy.jsp").forward(request, response);
        }
    }
}
