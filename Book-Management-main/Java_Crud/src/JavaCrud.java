import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JavaCrud {

	private JFrame frame;
	private JTextField txtbname;
	private JTextField txtauthor;
	private JTextField txtprice;
	private JTable table;
	private JTextField txtbid;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaCrud window = new JavaCrud();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JavaCrud() {
		initialize();
		Connect(); 
		table_load() ;
	}
	
	public void updateBook(String bname, String author, String price, String bid) throws SQLException {
		
		
		if (bname == null || bname.isEmpty()) {
	        throw new SQLException("Name cannot be empty");
	    }
	    if (author == null || author.isEmpty()) {
	        throw new SQLException("Author cannot be empty");
	    }
	    try {
	        double parsedPrice = Double.parseDouble(price);
	        if (parsedPrice <= 0) {
	            throw new SQLException("Price must be a positive value");
	        }
	    } catch (NumberFormatException e) {
	        throw new SQLException("Price must be a valid number");
	    }
	    if (bid == null || bid.isEmpty() || !bid.matches("\\d+")) {
	        throw new SQLException("Invalid ID");
	    }
		
		
		
		
	    pst = con.prepareStatement("update book set name= ?, author=?, price=? where id =?");
	    pst.setString(1, bname);
	    pst.setString(2, author);
	    pst.setString(3, price);
	    pst.setString(4, bid);
	    pst.executeUpdate();
	}
	
	public void addBook(String bname, String author, String price) throws SQLException {
		
		if (bname == null || bname.isEmpty()) {
	        throw new SQLException("Title cannot be null or empty");
	    }
	    if (author == null || author.isEmpty()) {
	        throw new SQLException("Author cannot be null or empty");
	    }
	   
	    
	    
	    double parsedPrice;
	    try {
	        parsedPrice = Double.parseDouble(price);
	    } catch (NumberFormatException e) {
	        throw new SQLException("Price must be a valid number");
	    }

	    // Check if price is a valid positive number
	    if (parsedPrice <= 0) {
	        throw new SQLException("Price must be a positive value");
	    }
	    
	    
	    
	    pst = con.prepareStatement("insert into book(name,author,price)values(?,?,?)");
	    pst.setString(1, bname);
	    pst.setString(2, author);
	    pst.setString(3, price);
	    pst.executeUpdate();
	}

	 public void delete(String id) throws SQLException {
	        String sql = "DELETE FROM book WHERE id = ?";
	        try (PreparedStatement pst = con.prepareStatement(sql)) {
	            pst.setString(1, id);
	            int rowsAffected = pst.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Book with ID " + id + " has been deleted.");
	            } else {
	            	
	            	
	            	
	            	throw new SQLException("No book found with ID " + id);
	            }
	        }
	    }
	


	public JTextField getTxtBname() {
	    return txtbname;
	}

	public JTextField getTxtAuthor() {
	    return txtauthor;
	}

	public JTextField getTxtPrice() {
	    return txtprice;
	}

	public JTextField getTxtBid() {
	    return txtbid;
	}

	
	
	
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	
	
	 public void Connect()
	    {
	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	            con = DriverManager.getConnection("jdbc:mysql://localhost/javacrud", "root","");
	        }
	        catch (ClassNotFoundException ex) 
	        {
	          ex.printStackTrace();
	        }
	        catch (SQLException ex) 
	        {
	               ex.printStackTrace();
	        }
	    }
	 
	 
	 
	 
	 
	 
	 

	 
	 
	 
	 
	 
	 public void table_load()
	 {
	     try 
	     {
	     pst = con.prepareStatement("select * from book");
	     rs = pst.executeQuery();
	     table.setModel(DbUtils.resultSetToTableModel(rs));
	 } 
	     catch (SQLException e) 
	      {
	         e.printStackTrace();
	   } 
	 }
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(238, 232, 238));
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 22));
		frame.setBounds(100, 100, 712, 472);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Book Shop");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setBounds(258, 11, 118, 35);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(238, 232, 238));
		panel.setBorder(new TitledBorder(null, "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(49, 85, 301, 152);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1.setBounds(29, 34, 106, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Author");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_2.setBounds(29, 75, 75, 20);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Price");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_3.setBounds(29, 124, 46, 14);
		panel.add(lblNewLabel_3);
		
		txtbname = new JTextField();
		txtbname.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtbname.setBounds(145, 31, 130, 20);
		panel.add(txtbname);
		txtbname.setColumns(10);
		
		txtauthor = new JTextField();
		txtauthor.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtauthor.setColumns(10);
		txtauthor.setBounds(145, 75, 130, 20);
		panel.add(txtauthor);
		
		txtprice = new JTextField();
		txtprice.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtprice.setColumns(10);
		txtprice.setBounds(145, 121, 130, 20);
		panel.add(txtprice);
		
		JButton btnNewButton_1 = new JButton("Clear");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			
				 txtbname.setText("");
			        txtauthor.setText("");
			        txtprice.setText("");
			        txtbname.requestFocus();
				
			}
		});
		btnNewButton_1.setBounds(256, 259, 94, 48);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnExit.setBounds(148, 259, 89, 48);
		frame.getContentPane().add(btnExit);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				 try {
			            String bname = txtbname.getText();
			            String author = txtauthor.getText();
			            String price = txtprice.getText();

			            addBook(bname, author, price); // Call the extracted method
			            JOptionPane.showMessageDialog(null, "Record Added!");
			            table_load();

			            txtbname.setText("");
			            txtauthor.setText("");
			            txtprice.setText("");
			            txtbname.requestFocus();
			        } catch (SQLException e1) {
			            e1.printStackTrace();
			            JOptionPane.showMessageDialog(null, "Error Adding Record: " + e1.getMessage());
			        }
			    }

		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton.setBounds(49, 259, 78, 48);
		frame.getContentPane().add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(360, 85, 326, 241);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(238, 232, 238));
		panel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(54, 332, 274, 76);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2_1 = new JLabel("Book ID");
		lblNewLabel_2_1.setBounds(20, 24, 70, 21);
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		panel_1.add(lblNewLabel_2_1);
		
		txtbid = new JTextField();
		txtbid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				
				
				  try {
				        String id = txtbid.getText();
				        if (id.isEmpty()) {
				            return; // Don't run the query if the ID field is empty
				        }

				        pst = con.prepareStatement("select name, author, price from book where id = ?");
				        pst.setString(1, id);
				        ResultSet rs = pst.executeQuery();

				        if (rs.next()) {
				            // If the book is found, set the fields
				            String name = rs.getString(1);
				            String author = rs.getString(2);
				            String price = rs.getString(3);

				            txtbname.setText(name);
				            txtauthor.setText(author);
				            txtprice.setText(price);
				        } else {
				            // If no book is found, clear the fields and show a message
				            txtbname.setText("");
				            txtauthor.setText("");
				            txtprice.setText("");
				            JOptionPane.showMessageDialog(null, "Book not found", "No Result", JOptionPane.INFORMATION_MESSAGE);
				        }
				    } catch (SQLException ex) {
				        ex.printStackTrace(); // Print the error for debugging
				        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
				    }
				
				
				
			}
		});
		txtbid.setBounds(96, 21, 146, 27);
		txtbid.setFont(new Font("Tahoma", Font.PLAIN, 17));
		txtbid.setColumns(10);
		panel_1.add(txtbid);
		
		JButton btnNewButton_1_1 = new JButton("Update");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				
				  
				  try {
			            String bname = txtbname.getText();
			            String author = txtauthor.getText();
			            String price = txtprice.getText();
			            String bid = txtbid.getText();

			            updateBook(bname, author, price, bid); // Call the extracted method
			            JOptionPane.showMessageDialog(null, "Record Updated!");
			            table_load();

			            txtbname.setText("");
			            txtauthor.setText("");
			            txtprice.setText("");
			            txtbname.requestFocus();
			        } catch (SQLException e1) {
			            e1.printStackTrace();
			            JOptionPane.showMessageDialog(null, "Error Updating Record: " + e1.getMessage());
			        }
			    }

		});
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton_1_1.setBounds(395, 337, 106, 35);
		frame.getContentPane().add(btnNewButton_1_1);
		
		JButton btnNewButton_1_2 = new JButton("Delete");
		btnNewButton_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				String bid = txtbid.getText();

		        // Check if the bid (book ID) is empty
		        if (bid.isEmpty()) {
		            JOptionPane.showMessageDialog(null, "Please enter a book ID.");
		            return;
		        }

		        try {
		            // Call the delete method from JavaCrud class
		            delete(bid);  

		            // Show success message
		            JOptionPane.showMessageDialog(null, "Record Deleted!");

		            // Refresh the table to reflect the deletion
		            table_load();

		            // Clear the text fields
		            txtbname.setText("");
		            txtauthor.setText("");
		            txtprice.setText("");
		            txtbname.requestFocus();

		        } catch (SQLException e1) {
		            // Handle any SQL exceptions
		            e1.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Error while deleting the record.");
		        }
		    }
		});
		btnNewButton_1_2.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton_1_2.setBounds(549, 337, 94, 35);
		frame.getContentPane().add(btnNewButton_1_2);
	}
}
