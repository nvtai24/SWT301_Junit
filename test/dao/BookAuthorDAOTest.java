package dao;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import dao.BookAuthorDAO;
import model.Author;

public class BookAuthorDAOTest {
    private BookAuthorDAO bookAuthorDAO;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws SQLException {
        mockConnection = Mockito.mock(Connection.class);
        mockStatement = Mockito.mock(PreparedStatement.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        bookAuthorDAO = new BookAuthorDAO();
        bookAuthorDAO.conn = mockConnection;
    }

    @Test
    public void testGetAllAuthors_Success() throws SQLException {
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        
        Mockito.when(mockResultSet.next()).thenReturn(true, true, false); // Giả lập có 2 bản ghi
        Mockito.when(mockResultSet.getInt("author_id")).thenReturn(1, 2);
        Mockito.when(mockResultSet.getString("image")).thenReturn("image1.jpg", "image2.jpg");
        Mockito.when(mockResultSet.getString("description")).thenReturn("desc1", "desc2");
        Mockito.when(mockResultSet.getString("author_name")).thenReturn("Author1", "Author2");
        
        List<Author> authors = bookAuthorDAO.getAllAuthor();
        
        assertNotNull(authors);
        assertEquals(2, authors.size());
        assertEquals("Author1", authors.get(0).getAuthorName());
        assertEquals("Author2", authors.get(1).getAuthorName());
    }

    @Test
    public void testGetAllAuthors_EmptyResultSet() throws SQLException {
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);
        
        List<Author> authors = bookAuthorDAO.getAllAuthor();
        
        assertNotNull(authors);
        assertTrue(authors.isEmpty());
    }

    @Test
    public void testGetAllAuthors_SQLException() throws SQLException {
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenThrow(new SQLException("Database error"));
        
        List<Author> authors = bookAuthorDAO.getAllAuthor();
        
        assertNotNull(authors);
        assertTrue(authors.isEmpty());
    }
}
