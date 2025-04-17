package gr.aueb.cf.pharmapp.controller;

import gr.aueb.cf.pharmapp.dto.UserLoginDTO;
import gr.aueb.cf.pharmapp.exceptions.UserDAOException;
import gr.aueb.cf.pharmapp.exceptions.UserNotFoundException;
import gr.aueb.cf.pharmapp.service.IPharmacyService;
import gr.aueb.cf.pharmapp.service.IUserService;
import gr.aueb.cf.pharmapp.service.UserServiceImpl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jboss.weld.context.http.Http;

import java.io.IOException;
import java.net.Authenticator;

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

        // Set headers to prevent caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        // Clear any previous error messages
        request.removeAttribute("error");
        request.removeAttribute("usernameMessage");
        request.removeAttribute("passwordMessage");

        // Data Binding
        String username = request.getParameter("username");
        String password = request.getParameter("password");



        try {
            // First check if username exists
            boolean usernameExists = userService.usernameExists(username);

            if (!usernameExists) {
                String usernameMessage = "Δεν υπάρχει χρήστης με το " +
                        "συγκεκριμένο username";
                request.setAttribute("usernameMessage", usernameMessage);
                request.setAttribute("username", username); // Keep the entered username
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }

            // Then attempt authentication
            UserLoginDTO userLoginDTO = new UserLoginDTO(username, password);
            boolean isAuthenticated = userService.authenticate(userLoginDTO);

            if (!isAuthenticated) {
                String passwordMessage = "Λάθος κωδικός πρόσβασης";
                request.setAttribute("passwordMessage", passwordMessage);
                request.setAttribute("username", username); // Keep the entered username
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }

            // If authentication succeeds
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("authenticated", true);
            session.setAttribute("username", username);
            session.setAttribute("role", userService.getUserByUsername(username).getRole());

            if (session.getAttribute("role").equals("ADMIN")) {
                session.setMaxInactiveInterval(ADMIN_TIMEOUT);
            }

            // Clear any error messages before redirecting
            request.removeAttribute("error");
            request.removeAttribute("usernameMessage");
            request.removeAttribute("passwordMessage");

            response.sendRedirect(request.getContextPath() + "/pharmapp/dashboard");

        } catch (UserDAOException | UserNotFoundException e) {
            String errorMessage = "Σφάλμα κατά την αυθεντικοποίηση";
            request.setAttribute("error", errorMessage);
            request.setAttribute("username", username); // Keep the entered username
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
