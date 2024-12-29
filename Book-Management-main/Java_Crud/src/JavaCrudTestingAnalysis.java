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

class JavaCrudTestingAnalysis {

	 private JavaCrud crud;
	    private Connection testConnection;
	    private JavaCrud app;

	    // Set up a test database connection before all tests.
	     
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
	            crud.con = testConnection; // Use test connection
	            crud.pst = null; // Reset prepared statement
	            app.Connect(); // Initialize database connection for app
	            // Clear the text fields using getters
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

	    // Test case for adding a book.
	     
	    @Test
	    void testAddBook() {
	        String bookName = "Test Book";
	        String author = "Test Author";
	        String price = "100";

	        try {
	            crud.addBook(bookName, author, price);

	            // Verify the book was added
	            PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE name=? AND author=? AND price=?");
	            pst.setString(1, bookName);
	            pst.setString(2, author);
	            pst.setString(3, price);
	            ResultSet rs = pst.executeQuery();

	            assertTrue(rs.next(), "The book should have been added to the database.");
	            assertEquals(bookName, rs.getString("name"));
	            assertEquals(author, rs.getString("author"));
	            assertEquals(price, rs.getString("price"));
	        } catch (SQLException e) {
	            fail("Exception occurred during testAddBook: " + e.getMessage());
	        }
	    }

	        //Test case for updating a book.
	     
	    @Test
	    void testUpdateBook() {
	        String bookName = "Updated Book";
	        String author = "Updated Author";
	        String price = "150";
	        String bookId = "41";

	        try {
	            // Add a sample book for testing
	            crud.addBook("Old Book", "Old Author", "50");

	            // Update the book
	            crud.updateBook(bookName, author, price, bookId);

	            // Verify the book was updated
	            PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE id=?");
	            pst.setString(1, bookId);
	            ResultSet rs = pst.executeQuery();

	            assertTrue(rs.next(), "The book should have been updated in the database.");
	            assertEquals(bookName, rs.getString("name"));
	            assertEquals(author, rs.getString("author"));
	            assertEquals(price, rs.getString("price"));
	        } catch (SQLException e) {
	            fail("Exception occurred during testUpdateBook: " + e.getMessage());
	        }
	    }

	    //Test case for table loading.
	   
	    
	 
	    @Test
	    void testTableLoad() {
	        try {
	            // Add sample data
	            crud.addBook("Book1", "Author1", "10");
	            //crud.addBook("Book2", "Author2", "20");

	            // Query to check if the book is in the database
	            PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE name=?");
	            pst.setString(1, "Book1"); 
	            ResultSet rs = pst.executeQuery();

	            // Search through the result set
	            boolean foundBook1 = false;
	            while (rs.next()) {
	                String name = rs.getString("name");
	                if ("Book1".equals(name)) {
	                    foundBook1 = true;
	                    break;
	                }
	            }

	            // Assert the book is found
	            assertTrue(foundBook1, "Book1 should be found in the database.");
	        } catch (SQLException e) {
	            fail("Exception occurred during testTableLoad: " + e.getMessage());
	        }
	    }




	    // Test case for invalid book addition (Condition Coverage).
	   
	    @Test
	    void testAddBookInvalid() {
	        String bookName = "";
	        String author = "";
	        String price = "";

	        assertThrows(SQLException.class, () -> {
	            crud.addBook(bookName, author, price);
	        }, "Adding a book with invalid data should throw an exception.");
	    }

	    // Test case for checking branch coverage in search functionality.
	    
	    @Test
	    void testSearchBook() {
	        String bookId = "3";  // We are now testing for id 3

	        try {
	            
	            // Search for the book with id = 3
	            PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE id=?");
	            pst.setString(1, bookId); 
	            ResultSet rs = pst.executeQuery();

	            assertTrue(rs.next(), "The book should be found in the database.");
	            assertEquals("ValidName",   rs.getString("name"));  
	            assertEquals("ValidAuthor", rs.getString("author"));  
	            assertEquals("100", rs.getString("price"));  

	        } catch (SQLException e) {
	            fail("Exception occurred during testSearchBook: " + e.getMessage());
	        }
	    }


	    // Test case for ensuring condition coverage (valid and invalid inputs).
	    @Test
	    void testAddBookConditionCoverage() {
	        String bookName = "ConditionTestBook";
	        String author = "ConditionTestAuthor";
	        String price = "500";

	        try {
	            // Valid Input
	            crud.addBook(bookName, author, price);
	            PreparedStatement pst = testConnection.prepareStatement("SELECT * FROM book WHERE name=?");
	            pst.setString(1, bookName);
	            ResultSet rs = pst.executeQuery();
	            assertTrue(rs.next(), "The book should have been added.");

	            // Invalid Input
	            assertThrows(SQLException.class, () -> crud.addBook("", "", ""), "Invalid input should throw an exception.");
	        } catch (SQLException e) {
	            fail("Exception occurred during testAddBookConditionCoverage: " + e.getMessage());
	        }
	    }

	    // Boundary Value Testing for addBook and updateBook
	    @Test
	    void testAddBook_BoundaryValues() {
	        // Test with a positive price
	        String name = "Test Book";
	        String author = "Test Author";
	        String validPrice = "10"; 

	        assertDoesNotThrow(() -> crud.addBook(name, author, validPrice),
	                "Book with valid price should be added successfully");

	        // Test with a negative price or zero (this should throw an exception)
	        String invalidPrice = "-10"; // or "0"
	        assertThrows(SQLException.class, () -> crud.addBook(name, author, invalidPrice),
	                "Book with invalid price should throw SQLException");
	    }

	    @Test
	    public void testUpdateBook_BoundaryValues() {
	        // Valid case: Price is a large positive value, should not throw any exception
	        assertDoesNotThrow(() -> {
	            app.updateBook("UpdatedBook", "UpdatedAuthor", "999999", "1");
	        });

	        // Invalid case: Price is negative, should throw an exception
	        assertThrows(SQLException.class, () -> {
	            app.updateBook("UpdatedBook", "UpdatedAuthor", "-1", "1"); // Invalid price (negative)
	        });
	    }


	    // Decision Table Testing for addBook and updateBook
	    @Test
	    public void testAddBook_DecisionTable() {
	        // T1: All valid inputs
	        assertDoesNotThrow(() -> app.addBook("ValidName", "ValidAuthor", "100"));

	        // T2: Name missing
	        assertThrows(SQLException.class, () -> app.addBook("", "ValidAuthor", "100"));

	        // T3: Author missing
	        assertThrows(SQLException.class, () -> app.addBook("ValidName", "", "100"));

	        // T4: Invalid price (negative)
	        assertThrows(SQLException.class, () -> app.addBook("ValidName", "ValidAuthor", "-100"));

	        // T5: Invalid price (non-numeric)
	        assertThrows(SQLException.class, () -> app.addBook("ValidName", "ValidAuthor", "abc"));
	    }

	    @Test
	    public void testUpdateBook_DecisionTable() {
	        // T1: All valid inputs
	        assertDoesNotThrow(() -> app.updateBook("ValidName", "ValidAuthor", "100", "1"));

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
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    @Test
	    void testAddBook_MCDC() {
	        // Test Case 1: bookName is valid, author is empty, price is valid
	        assertThrows(SQLException.class, () -> crud.addBook("ValidName", "", "100"), "Author cannot be empty");

	        // Test Case 2: bookName is empty, author is valid, price is valid
	        assertThrows(SQLException.class, () -> crud.addBook("", "ValidAuthor", "100"), "Book name cannot be empty");

	        // Test Case 3: bookName is valid, author is valid, price is invalid
	        assertThrows(SQLException.class, () -> crud.addBook("ValidName", "ValidAuthor", "abc"), "Price must be a valid number");

	        // Test Case 4: All conditions valid
	        assertDoesNotThrow(() -> crud.addBook("ValidName", "ValidAuthor", "100"), "Valid inputs should pass");
	    }

	    // Test Case for Modified Condition/Decision Coverage (MC/DC) for updateBook
	    @Test
	    void testUpdateBook_MCDC() {
	        String bookId = "41"; // Assume this ID exists in the test database.

	        // Test Case 1: Valid inputs
	        assertDoesNotThrow(() -> crud.updateBook("ValidName", "ValidAuthor", "200", bookId), 
	            "Valid inputs should not throw an exception.");

	        // Test Case 2: Empty book name
	        assertThrows(SQLException.class, 
	            () -> crud.updateBook("", "ValidAuthor", "200", bookId), 
	            "Empty book name should throw an exception.");

	        // Test Case 3: Empty author
	        assertThrows(SQLException.class, 
	            () -> crud.updateBook("ValidName", "", "200", bookId), 
	            "Empty author should throw an exception.");

	        // Test Case 4: Invalid price (non-numeric)
	        assertThrows(SQLException.class, 
	            () -> crud.updateBook("ValidName", "ValidAuthor", "abc", bookId), 
	            "Non-numeric price should throw an exception.");

	        // Test Case 5: Invalid book ID
	        assertThrows(SQLException.class, 
	            () -> crud.updateBook("ValidName", "ValidAuthor", "200", "abc"), 
	            "Non-numeric book ID should throw an exception.");
	    }



}
