package movie_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
    	 // Create the login frame with a fixed size
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 300);  // Small fixed window size
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);  // Prevent resizing

        // Main panel for the form with a background color and null layout for fixed positioning
        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 240, 255));  // Light blue background color
        panel.setLayout(null);  // Null layout for fixed positioning
        
     // Heading (Login)
        JLabel heading = new JLabel("Login");
        heading.setBounds(150, 10, 100, 30);  // Positioning at the top center
        heading.setFont(new Font("Arial", Font.BOLD, 20));  // Large font size for the heading
        panel.add(heading);

        // Username label and text field
        JLabel lblUsername = new JLabel("Username/Email:");
        lblUsername.setBounds(20, 60, 120, 25);  // Adjusted size and position
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblUsername);

        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(150, 60, 200, 25);  // Increased width and alignment
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtUsername);

        // Password label and text field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 100, 120, 25);  // Adjusted size and position
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 100, 200, 25);  // Increased width and alignment
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(txtPassword);

        // User type label and combo box
        JLabel lblUserType = new JLabel("Login as:");
        lblUserType.setBounds(20, 140, 120, 25);  // Adjusted size and position
        lblUserType.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblUserType);

        String[] userTypes = {"Admin", "Customer"};
        JComboBox<String> userTypeComboBox = new JComboBox<>(userTypes);
        userTypeComboBox.setBounds(150, 140, 200, 25);  // Increased width and alignment
        userTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(userTypeComboBox);

        // Login button with larger size
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 180, 100, 35);  // Enlarged button size and repositioned
        btnLogin.setBackground(new Color(76, 175, 80));  // Green background
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));  // Larger font
        panel.add(btnLogin);

        // Add panel to frame
        loginFrame.add(panel);
        loginFrame.setVisible(true);


        // Action Listener for Login Button
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String selectedUserType = (String) userTypeComboBox.getSelectedItem();

                // Call the login function to validate credentials
                if (selectedUserType.equals("Admin")) {
                    // Admin login
                    if (authenticateAdmin(username, password)) {
                        loginFrame.dispose(); // Close the login frame
                        new AdminDashboard();  // Open the Admin Dashboard
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid Admin credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (selectedUserType.equals("Customer")) {
                    // Customer login
                    int customerId = authenticateCustomer(username, password);
                    if (customerId != -1) {
                        loginFrame.dispose(); // Close the login frame
                        System.out.println("cus id"+customerId);
                        new CustomerPage(customerId);  // Pass customerId to CustomerDashboard
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid Customer credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    // Authenticate Admin based on the credentials
    private static boolean authenticateAdmin(String username, String password) {
        // Database connection details
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String query = "SELECT * FROM Admin WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);  // Here, we are not hashing the password for simplicity

            ResultSet rs = stmt.executeQuery();
            return rs.next();  // If any row is found, login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Authenticate Customer based on the credentials
    private static int authenticateCustomer(String username, String password) {
        // Database connection details
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String query = "SELECT customer_id FROM Customers WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);  // username is used as email in this case
            stmt.setString(2, password);  // Here, we are not hashing the password for simplicity

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("customer_id");  // Return the customer_id
            } else {
                return -1;  // Return -1 if no matching customer found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // Return -1 if error occurs
        }
    }
}
