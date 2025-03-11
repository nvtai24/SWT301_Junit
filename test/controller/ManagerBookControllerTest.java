package controller;

import dao.BookDAO;
import model.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ManagerBookControllerTest {

    @InjectMocks
    private ManagerBookController controller;

    @Mock
    private BookDAO bookDao;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    private List<Book> mockBooks;

    @Before
    public void setUp() {
        // Tạo danh sách sách giả lập
        mockBooks = new ArrayList<>();
        mockBooks.add(new Book(1, "Book 1", "Author 1", 10.99));
        mockBooks.add(new Book(2, "Book 2", "Author 2", 12.99));

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet_DefaultPagination_ShouldReturnBooks() throws ServletException, IOException {
        // Given: Mock request params chưa được thiết lập (mặc định)
        when(request.getParameter("page")).thenReturn(null);
        when(request.getParameter("pageSize")).thenReturn(null);
        when(request.getParameter("sort")).thenReturn(null);
        when(request.getParameter("categoryId")).thenReturn(null);
        when(request.getParameter("authorId")).thenReturn(null);
        when(request.getParameter("search")).thenReturn(null);

        // Given: Mock DAO trả về 20 sách tổng cộng và danh sách sách giả lập
        when(bookDao.getTotalBookCount()).thenReturn(20);
        when(bookDao.getBooksByPage(1, 8, 0)).thenReturn(mockBooks);

        // When: Gọi controller để xử lý doGet
        controller.doGet(request, response);

        // Then: Xác minh các attribute được set vào request
        verify(request).setAttribute("listBook", mockBooks);
        verify(request).setAttribute("currentPage", 1);
        verify(request).setAttribute("totalPages", 3);
        verify(request).setAttribute("pageSize", 8);
        verify(request).setAttribute("sort", "default");
        verify(request).setAttribute("categoryId", 0);

        // Then: Kiểm tra JSP chuyển hướng
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_SortByPriceAsc_ShouldReturnSortedBooks() throws ServletException, IOException {
        // Given: Mock request params với sort là "priceAsc" và categoryId là 1
        when(request.getParameter("sort")).thenReturn("priceAsc");
        when(request.getParameter("categoryId")).thenReturn("1");

        // Given: Mock DAO trả về danh sách sách đã được sắp xếp theo giá tăng dần
        when(bookDao.getBooksSortedByPriceAsc(1, 8, 1)).thenReturn(mockBooks);

        // When: Gọi controller để xử lý doGet
        controller.doGet(request, response);

        // Then: Xác minh các attribute được set vào request
        verify(request).setAttribute("listBook", mockBooks);
        verify(request).setAttribute("sort", "priceAsc");
        verify(request).setAttribute("categoryId", 1);

        // Then: Kiểm tra JSP chuyển hướng
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_Search_ShouldReturnFilteredBooks() throws ServletException, IOException {
        // Given: Mock request params với search là "Book"
        when(request.getParameter("search")).thenReturn("Book");

        // Given: Mock DAO trả về danh sách sách phù hợp với từ khóa tìm kiếm
        when(bookDao.getBooksByName("Book", 1, 8)).thenReturn(mockBooks);

        // When: Gọi controller để xử lý doGet
        controller.doGet(request, response);

        // Then: Xác minh các attribute được set vào request
        verify(request).setAttribute("listBook", mockBooks);
        verify(request).setAttribute("sort", "default");

        // Then: Kiểm tra JSP chuyển hướng
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_NoBooksFound_ShouldSetMessage() throws ServletException, IOException {
        // Given: Mock request params với search là "NoMatch"
        when(request.getParameter("search")).thenReturn("NoMatch");

        // Given: Mock DAO trả về danh sách rỗng khi không tìm thấy sách
        when(bookDao.getBooksByName("NoMatch", 1, 8)).thenReturn(new ArrayList<>());

        // When: Gọi controller để xử lý doGet
        controller.doGet(request, response);

        // Then: Xác minh thông báo "No books found" và danh sách sách rỗng được set vào request
        verify(request).setAttribute("mess", "No books found.");
        verify(request).setAttribute("listBook", new ArrayList<>());

        // Then: Kiểm tra JSP chuyển hướng
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_InvalidAuthorId_ShouldHandleGracefully() throws ServletException, IOException {
        // Given: Mock request params với authorId không hợp lệ (invalid)
        when(request.getParameter("authorId")).thenReturn("invalid");

        // When: Gọi controller để xử lý doGet mà không ném lỗi
        try {
            controller.doGet(request, response);
        } catch (ServletException | IOException e) {
            fail("Servlet should not throw exception on invalid authorId.");
        }
    }
}
