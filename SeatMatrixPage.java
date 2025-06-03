package movie_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;
public class SeatMatrixPage extends JFrame {
    private int selectedTheaterId;
    private String selectedDate;
    private String selectedTime;
    private int movieId;
    private int customerId;
    private List<JButton> seatButtons = new ArrayList<>();
    private Set<String> selectedSeats = new HashSet<>();
    private Set<String> bookedSeats = new HashSet<>();
    private Map<String, String> seatCategoryMap = new HashMap<>();
    private int showtimeId;

    public SeatMatrixPage(int showtimeId, int movieId, int customerId, int selectedTheaterId, String selectedDate, String selectedTime) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.customerId = customerId;
        this.selectedTheaterId = selectedTheaterId;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;

        setTitle("Seat Selection");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 5)); // Adjust grid layout for your seats

        // Load booked seats and display available ones
        loadBookedSeats();
        displaySeatsFromDatabase(panel);
        // Generate seat matrix with colors
        generateSeatMatrix(panel);

        // Next button for payment
        JButton nextButton = new JButton("book seats");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSeats.isEmpty()) {
                    JOptionPane.showMessageDialog(SeatMatrixPage.this, "Please select at least one seat.");
                } else {
                    saveSelectedSeats();
                    saveBookingDetails();
                }
            }
        });
        panel.add(nextButton);

        // Add the panel to the frame
        add(panel);
        setVisible(true);
    }

    // Method to load booked seats for the selected showtime
    private void loadBookedSeats() {
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String query = "SELECT seatname FROM booked_seats WHERE showtime_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, showtimeId);  // Filter by showtime ID
            ResultSet rs = stmt.executeQuery();

            // Add booked seats to the bookedSeats set
            while (rs.next()) {
                String seatName = rs.getString("seatname");
                bookedSeats.add(seatName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display available and booked seats dynamically as buttons
    private void displaySeatsFromDatabase(JPanel panel) {
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String sql = "SELECT seatname FROM seats WHERE seatname NOT IN (SELECT seatname FROM booked_seats WHERE showtime_id = ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, showtimeId);  // Filter by showtime ID
            ResultSet rs = stmt.executeQuery();

            // Add available seats to the seatCategoryMap
            while (rs.next()) {
                String seatName = rs.getString("seatname");
                seatCategoryMap.put(seatName, "Available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to generate the seat matrix (buttons)
    private void generateSeatMatrix(JPanel panel) {
        String[] allSeats = {"A1", "A2", "A3", "A4", "A5", "B1", "B2", "B3", "C1", "C2", "C3", "D1", "D2", "D3", "D4", "D5"};

        for (String seatName : allSeats) {
            JButton seatButton = new JButton(seatName);
            seatButtons.add(seatButton);

            // Set seat color based on availability
            if (bookedSeats.contains(seatName)) {
                seatButton.setBackground(Color.GRAY); // Booked seats are grey
                seatButton.setEnabled(false); // Disable booked seats
            } else if (seatCategoryMap.containsKey(seatName)) {
                seatButton.setBackground(Color.GREEN); // Available seats are green
                seatButton.setEnabled(true); // Enable available seats

                // Add action listener to select available seats
                seatButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (seatButton.getBackground() == Color.GREEN) {
                            if (selectedSeats.contains(seatName)) {
                                selectedSeats.remove(seatName);
                                seatButton.setBackground(Color.GREEN);
                            } else {
                                selectedSeats.add(seatName);
                                seatButton.setBackground(Color.YELLOW); // Highlight selected seats
                            }
                        }
                    }
                });
            }
            panel.add(seatButton);
        }
    }

    // Method to save the selected seats in the booked_seats table
    private void saveSelectedSeats() {
        String dbURL = "jdbc:mysql://localhost:3306/mini2";
        String dbUser = "root";
        String dbPassword = "mysql@cha!Pw";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String query = "INSERT INTO booked_seats (seatname, showtime_id, customer_id) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            for (String seatName : selectedSeats) {
                stmt.setString(1, seatName);
                stmt.setInt(2, showtimeId);
                stmt.setInt(3, customerId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 
 // Method to save the booking details in the bookings table
    private void saveBookingDetails() {
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";
        System.out.println("custid "+customerId+" showtimeid: "+showtimeId);
        double totalAmount = calculateTotalAmount();

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            // Insert booking details into bookings table
            String bookingQuery = "INSERT INTO bookings (customer_id, showtime_id, tot_amt) VALUES (?, ?, ?)";
            PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
            bookingStmt.setInt(1, customerId);
            bookingStmt.setInt(2, showtimeId);
            bookingStmt.setDouble(3, totalAmount);
            bookingStmt.executeUpdate();

            // Retrieve the booking details for displaying
            String query = "SELECT c.name, m.title AS movie_title, t.name AS theater_name, " +
                    "st.show_date, st.show_time " +
                    "FROM bookings b " +
                    "JOIN customers c ON c.customer_id = ? " +
                    "JOIN movies m ON m.movie_id = ? " +
                    "JOIN theaters t ON t.theater_id = ? " +
                    "JOIN showtimes st ON b.showtime_id = st.showtime_id " +
                    "WHERE b.showtime_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
        //    System.out.println("the cust id is "+customerId);
            stmt.setInt(1, customerId);
            stmt.setInt(2, movieId);
            stmt.setInt(3, selectedTheaterId);
            stmt.setInt(4, showtimeId);
//changed here c.customer_id 
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
               String id = rs.getString("name");  // Customer name with alias
                String movieTitle = rs.getString("movie_title");
                String theaterName = rs.getString("theater_name");  // Theater name with alias
                String showDate = rs.getString("show_date");  // Show date from showtimes table
                String showTime = rs.getString("show_time");  // Show time from showtimes table

                // Get the selected seats
                String selectedSeatsStr = String.join(", ", selectedSeats);

                // Debugging: print the values to console
                System.out.println("Customer: " + id);
                System.out.println("Movie: " + movieTitle);
                System.out.println("Theater: " + theaterName);
                System.out.println("Date: " + showDate);
                System.out.println("Time: " + showTime);
                System.out.println("Total Amount: " + totalAmount);
                System.out.println("Selected Seats: " + selectedSeatsStr);  // Print selected seats

                // Display booking details in a dialog box
                String ticketInfo = "Customer: " +id + "\n" +
                                    "Movie: " + movieTitle + "\n" +
                                    "Theater: " + theaterName + "\n" +
                                    "Date: " + showDate + "\n" +
                                    "Time: " + showTime + "\n" +
                                    "Total Amount: " + totalAmount + "\n" +
                                    "Seats: " + selectedSeatsStr;  // Add selected seats to the info

                JOptionPane.showMessageDialog(this, ticketInfo, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Method to calculate total amount (this can be based on seat categories or other business logic)
    private double calculateTotalAmount() {
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";
        double totalAmount = 0.0;

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            conn.setAutoCommit(false);  // Disable auto-commit

            // Call the stored procedure to calculate the total amount
            String procedureCall = "{CALL calculate_money1(?, ?, ?)}";
            CallableStatement stmt = conn.prepareCall(procedureCall);
            stmt.setInt(1, customerId); // Set customer_id
            stmt.setInt(2, showtimeId); // Set showtime_id
            stmt.registerOutParameter(3, java.sql.Types.DECIMAL); // Register the output parameter

            // Execute the stored procedure
            stmt.execute();

            // Retrieve the output parameter (total amount)
            totalAmount = stmt.getDouble(3);

            conn.commit();  // Commit the transaction

            System.out.println("Total Amount Calculated: " + totalAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalAmount; // Return the calculated total amount
    }


    public static void main(String[] args) {
       // new SeatMatrixPage(1, 1, 1, 1, "2024-11-14", "08:00:00");
    }
}
