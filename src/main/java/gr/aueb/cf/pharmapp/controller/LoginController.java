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
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        int ADMIN_TIMEOUT = 30 * 60; // 30 mins

        //Data Binding
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserLoginDTO userLoginDTO = new UserLoginDTO(username, password);

        boolean isAuthenticated = false;

        try{
            isAuthenticated = userService.authenticate(userLoginDTO);

            if (!isAuthenticated) {
                request.setAttribute("error", "Invalid credentials");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
                return;
            }

            HttpSession oldSession = request.getSession(false);

            if(oldSession != null){
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("authenticated", true);
            session.setAttribute("username", username);
            session.setAttribute("role",
                    userService.getUserByUsername(username).getRole());

            if(session.getAttribute("role").equals("ADMIN")){
                session.setMaxInactiveInterval(ADMIN_TIMEOUT);  //ADMIN gets
                // 30-min sessions
            }

            response.sendRedirect(request.getContextPath() + "/pharmapp" +
                    "/dashboard");

        }catch (UserDAOException | UserNotFoundException e){
            request.setAttribute("error", "Authentication Error");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
        }

    }
}
