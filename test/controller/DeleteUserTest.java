package controller;

import dao.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DeleteUserTest {

    private Delete deleteController;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        userDAO = mock(UserDAO.class);
        deleteController = new Delete(userDAO);
    }

    @Test
    public void testDoGet_DeleteUser() throws Exception {
        when(request.getParameter("delete")).thenReturn("deleteUser");
        when(request.getParameter("userId")).thenReturn("1");

        deleteController.doGet(request, response);

        verify(userDAO).deleteUserData(1);
        verify(response).sendRedirect("manager-user");
    }

    @Test
    public void testDoGet_DeleteUser_NoUserId() throws Exception {
        when(request.getParameter("delete")).thenReturn("deleteUser");
        when(request.getParameter("userId")).thenReturn(null);

        deleteController.doGet(request, response);

        verify(userDAO, never()).deleteUserData(anyInt());
        verify(response).sendRedirect("manager-user");
    }

    @Test
    public void testDoGet_InvalidDeleteAction() throws Exception {
        when(request.getParameter("delete")).thenReturn("invalidDelete");
        when(request.getParameter("userId")).thenReturn("1");

        // Gọi phương thức doGet
        deleteController.doGet(request, response);

        verify(userDAO, never()).deleteUserData(anyInt());
        verify(response).sendRedirect("manager-user");
    }

}
