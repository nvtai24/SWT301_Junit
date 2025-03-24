//package controller;
//
//import dao.UserDAO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.io.IOException;
//
//import static org.mockito.Mockito.*;
//
//public class AddAndEditAuthorControllerTest {
//
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @Mock
//    private UserDAO userDAO;
//
//    private AddAndEditAuthorController controller;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.openMocks(this); 
//        controller = new AddAndEditAuthorController(userDAO); 
//    }
//
//    @Test
//    public void testDoPostEditAuthor() throws ServletException, IOException {
//        when(request.getParameter("action")).thenReturn("edit");
//        when(request.getParameter("authorId")).thenReturn("1");
//        when(request.getParameter("authorName")).thenReturn("John Doe");
//        when(request.getParameter("authorImage")).thenReturn("image.jpg");
//        when(request.getParameter("authorDescription")).thenReturn("Test description");
//
//        controller.doPost(request, response);
//
//        verify(userDAO).updateAuthor(1, "John Doe", "image.jpg", "Test description");
//        verify(response).sendRedirect("manager-author");
//        verifyNoMoreInteractions(response);
//    }
//
//    @Test
//    public void testDoPostAddAuthor() throws ServletException, IOException {
//        when(request.getParameter("action")).thenReturn("add");
//        when(request.getParameter("authorName")).thenReturn("Jane Doe");
//        when(request.getParameter("authorImage")).thenReturn("image2.jpg");
//        when(request.getParameter("authorDescription")).thenReturn("New author description");
//
//        controller.doPost(request, response);
//
//        verify(userDAO).addAuthor("Jane Doe", "image2.jpg", "New author description");
//        verify(response).sendRedirect("manager-author");
//        verifyNoMoreInteractions(response);
//    }
//
//    @Test(expected = NumberFormatException.class)
//    public void testDoPostEditWithInvalidAuthorId() throws ServletException, IOException {
//        when(request.getParameter("action")).thenReturn("edit");
//        when(request.getParameter("authorId")).thenReturn("invalid_id");
//
//        controller.doPost(request, response);
//    }
//
//    @Test
//    public void testDoPostInvalidAction() throws ServletException, IOException {
//        when(request.getParameter("action")).thenReturn("invalid");
//
//        controller.doPost(request, response);
//
//        verifyNoInteractions(userDAO); // No DAO methods should be called
//        verify(response).sendRedirect("manager-author"); // Should still redirect
//    }
//}
