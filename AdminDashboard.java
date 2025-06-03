package movie_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AdminDashboard {

    public AdminDashboard() {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1)); // 4 buttons in 1 column

        JButton btnManageMovies = new JButton("Manage Movies");
       // JButton btnManageShowtimes = new JButton("Manage Time Slots");
       // JButton btnManageSeatPrices = new JButton("Manage Seat Prices");
        JButton btnLogout = new JButton("Logout");

        panel.add(btnManageMovies);
        //panel.add(btnManageShowtimes);
       // panel.add(btnManageSeatPrices);
        panel.add(btnLogout);

        frame.add(panel);
        frame.setVisible(true);

        // Action for "Manage Movies"
        btnManageMovies.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to open movie management options (Insert, Update, Delete, Display)
                new MovieManagementFrame();  // Create a new frame for managing movies
            }
        });


        // Action for Logout
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Close the application
            }
        });
    }

    public static void main(String[] args) {
        // This will be launched after successful login
    }
}
