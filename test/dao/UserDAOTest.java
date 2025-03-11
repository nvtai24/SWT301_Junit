package dao;
import dao.UserDAO;
import dal.DBConnect;
import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOTest {
    private static UserDAO userDAO;
    private static Connection conn;
    private int testAuthorId;

    @BeforeClass
    public static void setUp() {
        userDAO = new UserDAO();
        conn = new DBConnect().conn;
    }

    @Before
    public void insertTestData() {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Authors (author_name, image, description) VALUES (?, ?, ?);",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "Test Author");
            stmt.setString(2, "/assets/test-author.jpg");
            stmt.setString(3, "Test Author Description");
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    testAuthorId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to insert test author");
        }
    }

    @Test
    public void testAddAuthor() {
        userDAO.addAuthor("JUnit Author", "/assets/junit-author.jpg", "JUnit Test Description");
        
        boolean found = false;
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Authors WHERE author_name = ?")) {
            stmt.setString(1, "JUnit Author");
            ResultSet rs = stmt.executeQuery();
            found = rs.next();
        } catch (SQLException e) {
            fail("Failed to verify author insertion: " + e.getMessage());
        }
        assertTrue("Author should be added successfully", found);
    }

    @Test
    public void testUpdateAuthor() {
        userDAO.updateAuthor(testAuthorId, "Updated Author", "/assets/updated-author.jpg", "Updated Description");
        
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Authors WHERE author_id = ?")) {
            stmt.setInt(1, testAuthorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                assertEquals("Updated Author", rs.getString("author_name"));
                assertEquals("/assets/updated-author.jpg", rs.getString("image"));
                assertEquals("Updated Description", rs.getString("description"));
            } else {
                fail("Updated author not found");
            }
        } catch (SQLException e) {
            fail("Failed to verify author update: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteAuthor() {
        boolean isDeleted = userDAO.deleteAuthor(testAuthorId);
        assertTrue("Author should be deleted successfully", isDeleted);
        
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM Authors WHERE author_id = ?")) {
            stmt.setInt(1, testAuthorId);
            ResultSet rs = stmt.executeQuery();
            assertFalse("Deleted author should not exist", rs.next());
        } catch (SQLException e) {
            fail("Failed to verify author deletion: " + e.getMessage());
        }
    }

    @After
    public void cleanUpTestData() {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Authors WHERE author_id = ?")) {
            stmt.setInt(1, testAuthorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
