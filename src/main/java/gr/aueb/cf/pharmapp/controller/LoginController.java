package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.dto.UserLoginDTO;
import gr.aueb.cf.pharmapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.pharmapp.exceptions.UserDAOException;
import gr.aueb.cf.pharmapp.exceptions.UserNotFoundException;
import gr.aueb.cf.pharmapp.model.User;
import gr.aueb.cf.pharmapp.service.IPharmacyService;
import gr.aueb.cf.pharmapp.service.IUserService;
import gr.aueb.cf.pharmapp.service.UserServiceImpl;
import gr.aueb.cf.pharmapp.validator.UserValidator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jboss.weld.context.http.Http;

import java.io.IOException;
import java.net.Authenticator;
import java.util.Map;

@WebServlet("/login")
public class LoginController extends BaseServlet {

    private IUserService userService;

    @Override
    public void init() throws ServletException {
        EntityManagerFactory emf = getEntityManagerFactory();
        userService = new UserServiceImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request
                ,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int ADMIN_TIMEOUT = 30 * 60; // 30 mins

        // Clear previous attributes
        request.removeAttribute("error");
        request.removeAttribute("usernameMessage");
        request.removeAttribute("passwordMessage");

        // Data Binding
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        request.setAttribute("username", username);

        try {
            //Create DTO
            UserLoginDTO userLoginDTO = new UserLoginDTO(username, password);
            UserValidator validator = new UserValidator(getEntityManagerFactory());
            Map <String, String> errors = validator.validate(userLoginDTO);

            if(! errors.isEmpty()) {
                String usernameMessage = errors.getOrDefault("username", "");
                String passwordMessage = errors.getOrDefault("password", "");

                request.setAttribute("usernameMessage", usernameMessage);
                request.setAttribute("passwordMessage", passwordMessage);

                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }

            // If authentication succeeds
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            User user = userService.getUserEntityByUsername(username);
            session.setAttribute("authenticated", true);
            session.setAttribute("user", user);
            session.setAttribute("username", username);

            session.setAttribute("role", user.getRoleType().toString());

            if (session.getAttribute("role").equals("ADMIN")) {
                session.setMaxInactiveInterval(ADMIN_TIMEOUT);
            }

            response.sendRedirect(request.getContextPath() + "/pharmapp/dashboard");

        } catch (UserDAOException | UserNotFoundException e) {
            String errorMessage = "Σφάλμα κατά την αυθεντικοποίηση";
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
