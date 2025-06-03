package movie_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class BookNowPage extends JFrame {

    private JFrame frame;
    private JComboBox<String> theaterComboBox;
    private JComboBox<String> dateComboBox;
    private JComboBox<String> timeComboBox;
    private JButton btnNext;
    private Map<String, Integer> theaterMap;  // Map of theater name to ID
    private Map<String, Integer> showtimeMap;  // Map of showtime (time) to showtime_id
    private int movieId;  // Only the movieId passed from the previous page
    private int customerId;
    private int selectedTheaterId;  // Store selected theaterId

    // Constructor to initialize the page with the selected movieId
    public BookNowPage(int movieId, int customerId) {
        this.movieId = movieId;  // Store the passed movieId
        this.customerId = customerId;

        // Initialize frame and set properties
        frame = new JFrame("Book Tickets");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new BoxLayout(bookingPanel, BoxLayout.Y_AXIS));

        // Display movieId in the booking label (no need for movie details anymore)
        JLabel lblTitle = new JLabel("Booking for Movie ID: " + movieId);  // Display the movieId
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookingPanel.add(lblTitle);

        // Label for theater selection
        JLabel lblTheater = new JLabel("Select Theater:");
        bookingPanel.add(lblTheater);

        theaterComboBox = new JComboBox<>();
        loadTheaters();  // Load theaters from the database
        // Event listener to load available dates when a theater is selected
        theaterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAvailableDates();  // Load available dates when a theater is selected
            }
        });
        bookingPanel.add(theaterComboBox);

        // Label for date selection
        JLabel lblDate = new JLabel("Select Date:");
        bookingPanel.add(lblDate);

        dateComboBox = new JComboBox<>();
        // Event listener to dynamically load showtimes based on the selected theater and date
        dateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDate = (String) dateComboBox.getSelectedItem();
                if (selectedDate != null) {
                    loadShowtimes(selectedTheaterId, selectedDate);  // Load showtimes based on theater and date
                }
            }
        });
        bookingPanel.add(dateComboBox);

        // Label for time slot selection
        JLabel lblTime = new JLabel("Select Time Slot:");
        bookingPanel.add(lblTime);

        // Initialize timeComboBox before use
        timeComboBox = new JComboBox<>();
        bookingPanel.add(timeComboBox);

        // Button to proceed to the next step (Seat Selection)
        btnNext = new JButton("Next");
        btnNext.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTheater = (String) theaterComboBox.getSelectedItem();
                String selectedDate = (String) dateComboBox.getSelectedItem();
                String selectedTime = (String) timeComboBox.getSelectedItem();

                // Combine date and time into a single string in the format: "2024-11-15 09:00:00"
                String showDateTime = selectedDate + " " + selectedTime + ":00";  // Adding seconds

                try {
                    if (selectedTheater == null || selectedDate == null || selectedTime == null) {
                        JOptionPane.showMessageDialog(frame, "Please select all options.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int showtimeId = showtimeMap.getOrDefault(selectedTime, -1);  // Get the showtime_id from the map
                    if (showtimeId == -1) {
                        JOptionPane.showMessageDialog(frame, "No showtime available for this date and time.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
System.out.println("from boknowpage sent the customerid "+customerId);
                    // Pass the data (showtimeId, movieId, etc.) to the next page (SeatMatrixPage)
                    new SeatMatrixPage(showtimeId, movieId, customerId, selectedTheaterId, selectedDate, selectedTime);
                    frame.dispose();  // Close the current frame
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error processing the booking information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        bookingPanel.add(btnNext);

        frame.add(bookingPanel);
        frame.setVisible(true);
    }

    // Load theaters into the combo box
    private void loadTheaters() {
        theaterMap = new HashMap<>();  // Initialize the map to store theater names and their IDs
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mini2", "root", "mysql@cha!Pw")) {
            String query = "SELECT theater_id, name FROM theaters";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int theaterId = rs.getInt("theater_id");
                    String theaterName = rs.getString("name");
                    theaterMap.put(theaterName, theaterId);  // Store theater name and ID in the map
                    theaterComboBox.addItem(theaterName);  // Add theater name to the combo box
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load available dates from the database dynamically
    private void loadAvailableDates() {
        // Clear the date combo box before loading new dates
        dateComboBox.removeAllItems();
        
        String selectedTheater = (String) theaterComboBox.getSelectedItem();
        if (selectedTheater == null) return;  // If no theater selected, do nothing
        
        // Get theaterId based on the selected theater
        selectedTheaterId = theaterMap.get(selectedTheater);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mini", "root", "Ssu@2005")) {
            String query = "SELECT DISTINCT show_date FROM showtimes WHERE movie_id = ? AND theater_id = ? ORDER BY show_date";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, movieId);  // Use the selected movieId
                stmt.setInt(2, selectedTheaterId);  // Use the selected theaterId
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String showDate = rs.getString("show_date");
                    dateComboBox.addItem(showDate);  // Add the available dates to the combo box
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadShowtimes(int theaterId, String selectedDate) {
        showtimeMap = new HashMap<>();
        timeComboBox.removeAllItems();  // Clear previous time slots before loading new ones

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mini", "root", "Ssu@2005")) {
            String query = "SELECT showtime_id, show_time FROM showtimes WHERE theater_id = ? AND movie_id = ? AND show_date = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, theaterId);
                stmt.setInt(2, movieId);  // Use selected movieId
                stmt.setString(3, selectedDate);  // Use selected date
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int showtimeId = rs.getInt("showtime_id");
                    String showTime = rs.getString("show_time");
                    showtimeMap.put(showTime, showtimeId);  // Store showtime and ID mapping
                    timeComboBox.addItem(showTime);  // Add showtimes to the combo box
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example: Initialize with movie_id = 2 for testing purposes
      //  new BookNowPage(2, 2);  // Pass the movie_id as an argument (replace with actual selection logic in real use case)
    }
}
