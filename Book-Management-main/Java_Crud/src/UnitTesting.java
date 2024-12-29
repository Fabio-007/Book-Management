import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnitTesting {

	private JavaCrud crud;  
	 private Connection testConnection;
	    private JavaCrud app;
	    
	    @BeforeAll
	    static void setupClass() {
	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	        } catch (ClassNotFoundException e) {
	            fail("MySQL Driver not found.");
	        }
	    }

	    // Set up the test environment before each test.
	     
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

	    // Tear down the test environment after each test.
	    
	    @AfterEach
	    void tearDown() {
	        try {
	            if (testConnection != null) {
	                testConnection.close();
	            }
	        } catch (SQLException e) {
	            fail("Failed to close the test database connection: " + e.getMessage());
	        }
	    }
	    
	    
	   
	    @Test
	    void testDeleteBook() {
	        //Book ID to be deleted (ensure this ID is valid for testing)
	        String bookId = "10"; //Default value to be overwritten after adding a book

	        try {
	            System.out.println("Adding a new book...");
	            //Add a sample book for testing
	            crud.addBook("Book to Delete", "Author", "100");

	            System.out.println("Checking if the book exists in the database...");
	            // Verify the book is in the database by querying its ID
	            String queryCheck = "SELECT id FROM book WHERE name=?";
	            try (PreparedStatement pstCheck = testConnection.prepareStatement(queryCheck)) {
	                pstCheck.setString(1, "Book to Delete");
	                ResultSet rsCheck = pstCheck.executeQuery();

	                // Check if the book was added correctly, then get its ID
	                if (rsCheck.next()) {
	                    bookId = rsCheck.getString("id");
	                    System.out.println("Book ID found: " + bookId);
	                } else {
	                    fail("Failed to add the book to the database.");
	                }
	            }

	            System.out.println("Deleting the book...");
	            // Delete the book
	            crud.delete(bookId);

	            System.out.println("Verifying that the book was deleted...");
	            // Verify the book was deleted by querying the database again
	            String queryDeleteCheck = "SELECT * FROM book WHERE id=?";
	            try (PreparedStatement pstDeleteCheck = testConnection.prepareStatement(queryDeleteCheck)) {
	                pstDeleteCheck.setString(1, bookId);
	                ResultSet rsDeleteCheck = pstDeleteCheck.executeQuery();

	                //  Assert that the book is not in the database
	                
	                
	                assertFalse(rsDeleteCheck.next(), "The book should have been deleted from the database.");
	            }

	        } catch (SQLException e) {
	            fail("SQLException occurred during testDeleteBook: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    
	   
	    


	
	// Test case for deleting a book with invalid data (Condition Coverage).
	@Test
	void testDeleteBookInvalid() {
	    String invalidBookId = "999";  // Assuming this book ID doesn't exist

	    assertThrows(SQLException.class, () -> {
	        crud.delete(invalidBookId);
	    }, "Deleting a book that doesn't exist should throw an exception.");
	}
	
	@Test
	void testSearchBookById() {
	    String bookId = "3";  // A valid book ID

	    try {
	        // Ensure testConnection is initialized before executing the query
	        if (testConnection != null) {
	            // Search for the book
	            PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE id=?");
	            pst.setString(1, bookId);
	            ResultSet rs = pst.executeQuery();

	           
	            if (rs.next()) {
	                
	                System.out.println("Found book with ID " + bookId + ":");
	                System.out.println("Name: " + rs.getString("name"));
	                System.out.println("Author: " + rs.getString("author"));
	                System.out.println("Price: " + rs.getString("price"));

	                
	                assertEquals("Test Book", rs.getString("name"));
	                assertEquals("Test Author", rs.getString("author"));
	                assertEquals("10", rs.getString("price")) ;
	            } else {
	                fail("The book with ID " + bookId + " should be found.");
	            }
	        } else {
	            fail("Database connection was not initialized.");
	        }
	    } catch (SQLException e) {
	        fail("SQLException occurred during testSearchBookById: " + e.getMessage());
	    }
	}



	
	  @Test
	    void testSearchBookInvalidId() {
	        String invalidBookId = "999"; 

	        try {
	            // Ensure testConnection is initialized before executing the query
	            if (testConnection != null) {
	                // Search for a non-existent book
	                PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE id=?");
	                pst.setString(1, invalidBookId);
	                ResultSet rs = pst.executeQuery();

	                assertFalse(rs.next(), "The book should not be found with an invalid ID.");
	            } else {
	                fail("Database connection was not initialized.");
	            }
	        } catch (SQLException e) {
	            fail("Exception occurred during testSearchBookInvalidId: " + e.getMessage());
	        }
	    }

	// Test case for boundary testing when deleting a book with invalid data.
	@Test
	void testDeleteBookBoundaryValues() {
	    // Valid case: Attempting to delete a valid book ID
	    String validBookId = "12"; //change this to something that exists

	    assertDoesNotThrow(() -> crud.delete(validBookId),
	            "Book with valid ID should be deleted successfully");

	    // Invalid case: Attempting to delete a non-existent book ID
	    String invalidBookId = "-1"; // Invalid book ID
	    assertThrows(SQLException.class, () -> crud.delete(invalidBookId),
	            "Book with invalid ID should throw SQLException");
	}

	
	
	@Test
	void testAddBook_BoundaryValue() {
	    // Valid case: Price is a positive value
	    String validPrice = "10";
	    assertDoesNotThrow(() -> crud.addBook("Test Book", "Test Author", validPrice),
	            "Book with a valid price should be added successfully");

	    // Invalid case: Price is zero
	    String invalidPriceZero = "0";
	    assertThrows(SQLException.class, () -> crud.addBook("Test Book", "Test Author", invalidPriceZero),
	            "Price cannot be zero.");

	    // Invalid case: Negative price
	    String invalidPriceNegative = "-5";
	    assertThrows(SQLException.class, () -> crud.addBook("Test Book", "Test Author", invalidPriceNegative),
	            "Price cannot be negative.");
	}

	
	
	@Test
	void testUpdateBook_DecisionTable_InvalidInput() {
	    // T1: Valid update (name, author, price)
	    assertDoesNotThrow(() -> app.updateBook("UpdatedName", "UpdatedAuthor", "100", "1"));

	    // T2: Name missing
	    assertThrows(SQLException.class, () -> app.updateBook("", "ValidAuthor", "100", "1"));

	    // T3: Author missing
	    assertThrows(SQLException.class, () -> app.updateBook("ValidName", "", "100", "1"));

	    // T4: Invalid price (negative)
	    assertThrows(SQLException.class, () -> app.updateBook("ValidName", "ValidAuthor", "-100", "1"));

	    // T5: Invalid price (non-numeric)
	    assertThrows(SQLException.class, () -> app.updateBook("ValidName", "ValidAuthor", "abc", "1"));

	    // T6: Invalid ID
	    assertThrows(SQLException.class, () -> app.updateBook("ValidName", "ValidAuthor", "100", "abc"));
	}

	
	
	
	
	
	
	
}
