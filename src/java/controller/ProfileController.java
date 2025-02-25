package controller;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;


public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("username");
        UserDAO userDao = new UserDAO();
        User usertemp = userDao.getUserByUserId(user.getUserId());

        session.setAttribute("username", usertemp);

        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("username");

        UserDAO userDao = new UserDAO();

        userDao.validateUser(user.getUsername(), password);

        boolean updateSuccessful = userDao.updateUser(user.getUserId(), email, password, confirmPassword, fullName);
        if (updateSuccessful) {
            request.setAttribute("updateMessage", "Profile updated successfully.");
        } else {
            request.setAttribute("updateMessage", "Failed to update profile. Please check your inputs.");
        }

        doGet(request, response);
    }
}
