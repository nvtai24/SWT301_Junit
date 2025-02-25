package controller;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");

        if (username == null || username.isEmpty()
                || email == null || email.isEmpty()
                || password == null || password.isEmpty()
                || confirmPassword == null || confirmPassword.isEmpty()
                || fullName == null || fullName.isEmpty()) {

            request.setAttribute("registerError", "All fields are required.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("registerError", "Passwords do not match.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        UserDAO userDao = new UserDAO();
        boolean isCreated = userDao.createUser(username, password, fullName, email);
        if (isCreated) {
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("registerError", "Registration failed. Username or email might already be taken.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
