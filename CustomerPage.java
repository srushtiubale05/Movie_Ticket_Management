package movie_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.*;

public class CustomerPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public CustomerPage(int customerId) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout());

        // Button to explore movies
        JButton btnExploreMovies = new JButton("Explore Movies");
        btnExploreMovies.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the CustomerDashboard JFrame
                new CustomerDashboard(customerId);
            }
        });

        // Button to view profile
        JButton btnViewProfile = new JButton("View Profile");
        btnViewProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCustomerProfile(customerId);
            }
        });

        // Add buttons to the content pane
        contentPane.add(btnExploreMovies);
        contentPane.add(btnViewProfile);

        // Make the CustomerPage frame visible
        setVisible(true);
    }

    // Method to display customer profile information
    private void displayCustomerProfile(int customerId) {
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String query = "SELECT * FROM customers WHERE customer_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                String address = rs.getString("address");

                String customerDetails = "<html><b>Name:</b> " + name + "<br>" +
                        "<b>Email:</b> " + email + "<br>" +
                        "<b>Phone:</b> " + phone + "<br>" +
                        "<b>Address:</b> " + address + "</html>";

                JOptionPane.showMessageDialog(this, customerDetails, "Customer Profile", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Customer details not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CustomerPage frame = new CustomerPage(1); // Pass a valid customer ID here
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
