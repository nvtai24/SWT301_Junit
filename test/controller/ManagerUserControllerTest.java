package controller;

import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ManagerUserControllerTest {

    private ManagerUserController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private UserDAO userDao;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
        userDao = mock(UserDAO.class);
        controller = new ManagerUserController(userDao);
    }

    @Test
    public void testDoGet_WithPagination() throws ServletException, IOException, SQLException {
        when(request.getParameter("page")).thenReturn("2");

        when(userDao.getTotalUsers()).thenReturn(25);

        List<User> sampleUsers = new ArrayList<>();
        sampleUsers.add(new User(1, "user1", "email1@example.com", "active"));
        sampleUsers.add(new User(2, "user2", "email2@example.com", "banned"));

        when(userDao.getUsersByPage(2, 10)).thenReturn(sampleUsers);

        when(request.getRequestDispatcher("managerUser.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(request).setAttribute(eq("listUser"), eq(sampleUsers));
        verify(request).setAttribute(eq("totalPages"), eq(3));
        verify(request).setAttribute(eq("currentPage"), eq(2));

        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_DefaultPage() throws ServletException, IOException, SQLException {
        when(request.getParameter("page")).thenReturn(null);

        when(userDao.getTotalUsers()).thenReturn(30);
        List<User> sampleUsers = new ArrayList<>();
        sampleUsers.add(new User(1, "user1", "email1@example.com", "active"));

        when(userDao.getUsersByPage(1, 10)).thenReturn(sampleUsers);

        when(request.getRequestDispatcher("managerUser.jsp")).thenReturn(dispatcher);

        controller.doGet(request, response);

        verify(request).setAttribute(eq("listUser"), eq(sampleUsers));
        verify(request).setAttribute(eq("totalPages"), eq(3));
        verify(request).setAttribute(eq("currentPage"), eq(1));

        verify(dispatcher).forward(request, response);
    }

}
