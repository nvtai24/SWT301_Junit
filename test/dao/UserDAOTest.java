
package dao;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAOTest {
    private UserDAO userDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @Before
    public void setUp() throws Exception {
        mockConnection = Mockito.mock(Connection.class);
        mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        userDAO = new UserDAO();
        userDAO.conn = mockConnection;
    }

    @Test
    public void testUpdateAuthor_Success() throws SQLException {
        String updateQuery = "UPDATE Authors SET author_name = ?, image = ?, description = ? WHERE author_id = ?";
        Mockito.when(mockConnection.prepareStatement(updateQuery)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        userDAO.updateAuthor(1, "New Author", "new_image.jpg", "Updated description");

        Mockito.verify(mockPreparedStatement).setString(1, "New Author");
        Mockito.verify(mockPreparedStatement).setString(2, "new_image.jpg");
        Mockito.verify(mockPreparedStatement).setString(3, "Updated description");
        Mockito.verify(mockPreparedStatement).setInt(4, 1);
        Mockito.verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateAuthor_Failure() throws SQLException {
        String updateQuery = "UPDATE Authors SET author_name = ?, image = ?, description = ? WHERE author_id = ?";
        Mockito.when(mockConnection.prepareStatement(updateQuery)).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        userDAO.updateAuthor(1, "New Author", "new_image.jpg", "Updated description");

        Mockito.verify(mockPreparedStatement).executeUpdate();
    }
}
