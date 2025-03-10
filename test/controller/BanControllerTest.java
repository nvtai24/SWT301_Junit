package controller;

import dao.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BanControllerTest {

    private BanController banController;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserDAO userDao;

    @Before
    public void setUp() {
        // Khởi tạo đối tượng controller, request, response và mock UserDAO
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        userDao = mock(UserDAO.class);
        banController = new BanController(userDao);
    }

    @Test
    public void testDoGet() throws Exception {
        when(request.getParameter("UserId")).thenReturn("1");

        when(userDao.banUserId(1)).thenReturn(true);

        banController.doGet(request, response);

        verify(userDao).banUserId(1);

        verify(response).sendRedirect("manager-user");
    }

}
