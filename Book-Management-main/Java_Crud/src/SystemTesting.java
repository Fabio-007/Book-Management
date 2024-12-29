import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemTesting {
	
	
	private JavaCrud crud;  
	private Connection testConnection;
	private JavaCrud app;
	
    @BeforeAll
    static void setupClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            fail("MySQL Driver not found.");
        }
    }

    
    @BeforeEach
    void setup() {
        crud = new JavaCrud();
        app = new JavaCrud();
        try {
            // Connect to the test database
            testConnection = DriverManager.getConnection("jdbc:mysql://localhost/javacrud", "root", "");
            crud.con = testConnection; 
            crud.pst = null; 
            app.Connect(); 

            
            app.getTxtBname().setText("");
            app.getTxtAuthor().setText("");
            app.getTxtPrice().setText("");
            app.getTxtBid().setText("");
        } catch (SQLException e) {
            fail("Failed to connect to the test database: " + e.getMessage());
        }
    }

    // Cleanup after each test
    @AfterEach
    void cleanUp() throws SQLException {
        // Only delete test data to ensure no important records are deleted
        PreparedStatement pst = testConnection.prepareStatement("DELETE FROM book WHERE name LIKE ?");
        pst.setString(1, "Test Book%"); 
        pst.executeUpdate();
    }
	
	
	
	@Test
	void testCreateBook() throws SQLException {
	    crud.addBook("New Book", "Author Name", "20.00");

	    // Verify that the book is inserted
	    PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE name = ?");
	    pst.setString(1, "New Book");
	    ResultSet rs = pst.executeQuery();
	    assertTrue(rs.next(), "The book should be found by name.");
	}

	
	
	
	
	@Test
	void testReadBook() throws SQLException {
	    String bookId = "5";
	    PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE id = ?");
	    pst.setString(1, bookId);
	    ResultSet rs = pst.executeQuery();

	    assertTrue(rs.next(), "The book should be found by ID.");
	    assertEquals("New Book", rs.getString("name"), "The book name should match.");
	}


	
	
	
	
	
	@Test
	void testUpdateBook() throws SQLException {
	    String bookId = "2";  
	    String updatedPrice = "100";  

	    try {
	        // Assert that the method does not throw any exception
	        assertDoesNotThrow(() -> app.updateBook("ValidName", "ValidAuthor", updatedPrice, bookId));

	        // Verify that the book's price was updated
	        PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE id = ?");
	        pst.setString(1, bookId);
	        ResultSet rs = pst.executeQuery();

	       
	        assertTrue(rs.next(), "The book should be found by ID.");
	        
	     
	        assertEquals(updatedPrice, rs.getString("price"), "The book's price should be updated to " + updatedPrice);

	    } catch (SQLException e) {
	        e.printStackTrace();
	        fail("SQLException: " + e.getMessage());
	    }
	}

	@Test
	void testDeleteBook() throws SQLException {
	    crud.delete("8"); 

	    // Verify that the book was deleted
	    PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE id = ?");
	    pst.setString(1, "8");
	    ResultSet rs = pst.executeQuery();
	    assertFalse(rs.next(), "The book should be deleted.");
	}

	
	
	@Test
	void testDeleteNonExistentBook() {
	    String invalidBookId = "999"; // Assuming this book ID doesn't exist

	    assertThrows(SQLException.class, () -> crud.delete(invalidBookId),
	            "Deleting a non-existent book should throw SQLException");
	}

	

	
	
	
	
}
