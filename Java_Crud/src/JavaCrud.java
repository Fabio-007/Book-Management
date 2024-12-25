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
				
				
				
				
				
				 String bname,author,price;
				    bname = txtbname.getText();
				    author = txtauthor.getText();
				    price = txtprice.getText();
				                
				     try {
				        pst = con.prepareStatement("insert into book(name,author,price)values(?,?,?)");
				        pst.setString(1, bname);
				        pst.setString(2, author);
				        pst.setString(3, price);
				        pst.executeUpdate();
				        JOptionPane.showMessageDialog(null, "Record Addedddd!!!!!");
				        table_load();
				                       
				        txtbname.setText("");
				        txtauthor.setText("");
				        txtprice.setText("");
				        txtbname.requestFocus();
				       }
				    catch (SQLException e1) 
				        {            
				       e1.printStackTrace();
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
                        pst = con.prepareStatement("select name,author,price from book where id = ?");
                        pst.setString(1, id);
                        ResultSet rs = pst.executeQuery();
                    if(rs.next()==true)
                    {
                      
                        String name = rs.getString(1);
                        String author = rs.getString(2);
                        String price = rs.getString(3);
                        
                        txtbname.setText(name);
                        txtauthor.setText(author);
                        txtprice.setText(price);

                    }   
                    else
                    {
                        txtbname.setText("");
                        txtauthor.setText("");
                        txtprice.setText("");
                         
                    }
                } 
            
             catch (SQLException ex) {
                   
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
				
				
				
				
String bname,author,price,bid;
                
                
                bname = txtbname.getText();
                author = txtauthor.getText();
                price = txtprice.getText();
                bid  = txtbid.getText();
                
                 try {
                        pst = con.prepareStatement("update book set name= ?,author=?,price=? where id =?");
                        pst.setString(1, bname);
                        pst.setString(2, author);
                        pst.setString(3, price);
                        pst.setString(4, bid);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Record Update!!!!!");
                        table_load();
                       
                        txtbname.setText("");
                        txtauthor.setText("");
                        txtprice.setText("");
                        txtbname.requestFocus();
                    }
                    catch (SQLException e1) {
                        
                        e1.printStackTrace();
                    }
			}
		});
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton_1_1.setBounds(395, 337, 106, 35);
		frame.getContentPane().add(btnNewButton_1_1);
		
		JButton btnNewButton_1_2 = new JButton("Delete");
		btnNewButton_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				
				String bid;
		           bid  = txtbid.getText();
		           
		            try {
		                   pst = con.prepareStatement("delete from book where id =?");
		           
		                   pst.setString(1, bid);
		                   pst.executeUpdate();
		                   JOptionPane.showMessageDialog(null, "Record Delete!!!!!");
		                   table_load();
		                  
		                   txtbname.setText("");
		                   txtauthor.setText("");
		                   txtprice.setText("");
		                   txtbname.requestFocus();
		                   
		                   
		                   
		                   
		                   
		                   
		                   
		                   
		               }
		               catch (SQLException e1) {
		                   
		                   e1.printStackTrace();
		               }
			}
		});
		btnNewButton_1_2.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton_1_2.setBounds(549, 337, 94, 35);
		frame.getContentPane().add(btnNewButton_1_2);
	}
}
