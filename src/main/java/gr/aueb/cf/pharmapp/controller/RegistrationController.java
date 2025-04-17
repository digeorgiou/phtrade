package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.dto.UserInsertDTO;
import gr.aueb.cf.pharmapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.pharmapp.exceptions.UserAlreadyExistsException;
import gr.aueb.cf.pharmapp.exceptions.UserDAOException;
import gr.aueb.cf.pharmapp.service.IUserService;
import gr.aueb.cf.pharmapp.service.UserServiceImpl;
import gr.aueb.cf.pharmapp.validator.UserValidator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/register")
public class RegistrationController extends BaseServlet {

    private IUserService userService;

    @Override
    public void init() throws ServletException {
        EntityManagerFactory emf = getEntityManagerFactory();
        userService = new UserServiceImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserInsertDTO userDTO;

        // Get form data
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        boolean terms = "on".equals(request.getParameter("terms"));


        String errorMessage = "";
        Map<String, String> errors;

        String usernameMessage;
        String passwordMessage;
        String confirmPasswordMessage;
        String emailMessage;
        String termsMessage;

        try {
            // Create DTO
            userDTO = new UserInsertDTO();
            userDTO.setUsername(username);
            userDTO.setEmail(email);
            userDTO.setPassword(password);
            userDTO.setConfirmedPassword(confirmPassword);
            userDTO.setTermsAccepted(terms);
            userDTO.setRole("PHARMACY_USER"); // Default role

            UserValidator<UserInsertDTO> validator =
                    new UserValidator<>(getEntityManagerFactory());

            errors = validator.validate(userDTO);


            if(! errors.isEmpty()) {
                usernameMessage = errors.getOrDefault("username", "");
                passwordMessage = errors.getOrDefault("password","");
                confirmPasswordMessage = errors.getOrDefault("confirmPassword", "");
                emailMessage = errors.getOrDefault("email", "");
                termsMessage = errors.getOrDefault("terms", "");


                request.setAttribute("termsMessage",termsMessage);
                request.setAttribute("usernameMessage", usernameMessage);
                request.setAttribute("passwordMessage", passwordMessage);
                request.setAttribute("confirmPasswordMessage", confirmPasswordMessage);
                request.setAttribute("emailMessage", emailMessage);

                request.setAttribute("userRegisterDTO", userDTO);

                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request,response);
                return;
            }

            // Register user
            UserReadOnlyDTO registeredUser = userService.insertUser(userDTO);

            // Auto-login after registration
            HttpSession session = request.getSession(true);
            session.setAttribute("authenticated", true);
            session.setAttribute("userInfo", registeredUser);

            // Redirect to registration success
            request.getRequestDispatcher("/WEB-INF/jsp/registration-success.jsp").forward(request, response);

        }  catch (UserDAOException | UserAlreadyExistsException e) {
            errorMessage = e.getMessage();
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
}
