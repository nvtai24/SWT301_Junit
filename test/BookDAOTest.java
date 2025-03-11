import dao.BookDAO;
import dao.BookDAO.InvalidBookDetailsException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BookDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @InjectMocks
    private BookDAO bookDAO;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
    }

    @Test(expected = InvalidBookDetailsException.class)
    public void testAddNewBook_InvalidRating() throws Exception {
        // Given : Rating invalid
        int categoryId = 1;
        String title = "Mockito for Beginners";
        int rating = 6;  
        double price = 15.99;
        String description = "An introductory book about Mockito.";
        String image = "mockito-beginners.jpg";
        int quantity_in_stock = 100;

        // When 
        bookDAO.addNewBook(categoryId, title, rating, price, description, image, quantity_in_stock);

        // Then : The test will pass if InvalidBookDetailsException is thrown
    }

    @Test(expected = InvalidBookDetailsException.class)
    public void testAddNewBook_InvalidPrice() throws Exception {
         // Given :  Price invalid
        int categoryId = 1;
        String title = "Mockito for Beginners";
        int rating = 4;
        double price = -1.99;  
        String description = "An introductory book about Mockito.";
        String image = "mockito-beginners.jpg";
        int quantity_in_stock = 100;

       // When 
        bookDAO.addNewBook(categoryId, title, rating, price, description, image, quantity_in_stock);

         // Then : The test will pass if InvalidBookDetailsException is thrown
    }

    @Test(expected = InvalidBookDetailsException.class)
    public void testAddNewBook_InvalidQuantityInStock() throws Exception {
        // Given :   Quantity invalid
        int categoryId = 1;
        String title = "Mockito for Beginners";
        int rating = 4;
        double price = 15.99;
        String description = "An introductory book about Mockito.";
        String image = "mockito-beginners.jpg";
        int quantity_in_stock = -10; 

        // When 
        bookDAO.addNewBook(categoryId, title, rating, price, description, image, quantity_in_stock);

         // Then : The test will pass if InvalidBookDetailsException is thrown
    }

    @Test
    public void testAddNewBook_Success() throws Exception {
        // Given:  Data valid 
        int categoryId = 1;
        String title = "Mockito for Beginners";
        int rating = 4;
        double price = 15.99;
        String description = "An introductory book about Mockito.";
        String image = "mockito-beginners.jpg";
        int quantity_in_stock = 100;

        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1); 

        // When
        boolean result = bookDAO.addNewBook(categoryId, title, rating, price, description, image, quantity_in_stock);

        // Then
        assertTrue(result);
    }

    @Test
    public void testAddNewBook_Failure() throws Exception {
        // Given:  Data valid , fail PreparedStatement
        int categoryId = 1;
        String title = "Mockito for Experts";
        int rating = 5;
        double price = 19.99;
        String description = "Advanced Mockito guide";
        String image = "mockito-expert.jpg";
        int quantity_in_stock = 5;

        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(0);  

         // When
        boolean result = bookDAO.addNewBook(categoryId, title, rating, price, description, image, quantity_in_stock);

        // Then
        assertFalse(result);
    }


}
