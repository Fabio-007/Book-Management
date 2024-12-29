import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegrationTesting {

	private JavaCrud javaCrud;

    @BeforeEach
    void setUp() throws SQLException {
        javaCrud = new JavaCrud();
        javaCrud.Connect();
        resetDatabase();
    }

    private void resetDatabase() throws SQLException {
        // Clean up and reset database state
        try (Statement stmt = javaCrud.con.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS book");
            stmt.executeUpdate("CREATE TABLE book (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), author VARCHAR(255), price DECIMAL)");
            stmt.executeUpdate("INSERT INTO book (name, author, price) VALUES ('Book1', 'Author1', 10.0)");
            stmt.executeUpdate("INSERT INTO book (name, author, price) VALUES ('Book2', 'Author2', 15.0)");
        }
    }

    @Test
    void testTableLoad() throws SQLException {
        // Execute the table_load method
        javaCrud.table_load();

        // Validate that data is present in the table
        try (Statement stmt = javaCrud.con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM book")) {
            if (rs.next()) {
                int rowCount = rs.getInt(1);
                assertEquals(2, rowCount, "There should be 2 rows in the book table.");
            }
        }
    }

    @Test
    void testAddBook() throws SQLException {
        // Add a new book
        javaCrud.addBook("New Book", "New Author", "25.0");

        // Verify if the book is added by checking the row count
        try (Statement stmt = javaCrud.con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM book")) {
            if (rs.next()) {
                int rowCount = rs.getInt(1);
                assertEquals(3, rowCount, "A new book should be added to the database.");
            }
        }
    }

    @Test
    void testUpdateBook() throws SQLException {
        // Update a book
        javaCrud.updateBook("Updated Book", "Updated Author", "20.0", "1");

        // Verify if the book was updated
        try (PreparedStatement pst = javaCrud.con.prepareStatement("SELECT name, author, price FROM book WHERE id = ?")) {
            pst.setInt(1, 1);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    assertEquals("Updated Book", rs.getString("name"));
                    assertEquals("Updated Author", rs.getString("author"));
                    assertEquals(20.0, rs.getDouble("price"), 0.01, "Price should be updated.");
                }
            }
        }
    }

    @Test
    void testDeleteBook() throws SQLException {
        // Delete a book
        javaCrud.delete("1");

        // Verify the book is deleted
        try (PreparedStatement pst = javaCrud.con.prepareStatement("SELECT COUNT(*) FROM book WHERE id = ?")) {
            pst.setInt(1, 1);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    assertEquals(0, rs.getInt(1), "Book should be deleted from the database.");
                }
            }
        }
    }

}
