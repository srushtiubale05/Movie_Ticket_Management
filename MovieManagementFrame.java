package movie_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class MovieManagementFrame extends JFrame {
    private JButton insertBtn, updateBtn, displayBtn, deleteBtn;
    private JTable movieTable;
    private DefaultTableModel tableModel;

    // Database connection info
    private final String dbURL = "jdbc:mysql://localhost:3306/mini";
    private final String dbUser = "root";
    private final String dbPassword = "Ssu@2005";

    public MovieManagementFrame() {
        setTitle("Movie Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel for main buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        insertBtn = new JButton("Insert");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        displayBtn = new JButton("Display");

        buttonPanel.add(insertBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(displayBtn);

        // Table to display movies
        tableModel = new DefaultTableModel();
        movieTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(movieTable);

        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set up button actions
        insertBtn.addActionListener(e -> insertMovie());
        updateBtn.addActionListener(e -> updateMovie());
        deleteBtn.addActionListener(e -> deleteMovie());
        displayBtn.addActionListener(e -> displayMovies());

        setVisible(true);
    }

    private void insertMovie() {
        String movieId = JOptionPane.showInputDialog(this, "Enter Movie ID for the new movie:");

        if (movieId != null && !movieId.trim().isEmpty()) {
            JTextField titleField = new JTextField();
            JTextField genreField = new JTextField();
            JTextField languageField = new JTextField();
            JTextField durationField = new JTextField();
            JTextField dateField = new JTextField();
            JTextField ratingField = new JTextField();
            JTextField directorField = new JTextField();
            JTextField castField = new JTextField();
            JTextArea descriptionField = new JTextArea(3, 20);

            JPanel panel = new JPanel(new GridLayout(10, 2));
            panel.add(new JLabel("Movie ID:"));
            panel.add(new JLabel(movieId)); // Display movie ID here
            panel.add(new JLabel("Title:"));
            panel.add(titleField);
            panel.add(new JLabel("Genre:"));
            panel.add(genreField);
            panel.add(new JLabel("Language:"));
            panel.add(languageField);
            panel.add(new JLabel("Duration (minutes):"));
            panel.add(durationField);
            panel.add(new JLabel("Release Date (YYYY-MM-DD):"));
            panel.add(dateField);
            panel.add(new JLabel("Rating:"));
            panel.add(ratingField);
            panel.add(new JLabel("Director:"));
            panel.add(directorField);
            panel.add(new JLabel("Cast:"));
            panel.add(castField);
            panel.add(new JLabel("Description:"));
            panel.add(new JScrollPane(descriptionField));

            int result = JOptionPane.showConfirmDialog(this, panel, "Enter Movie Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
                    String query = "INSERT INTO movies (movie_id, title, genre, language, duration, release_date, rating, director, cast, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setInt(1, Integer.parseInt(movieId)); // Set movie_id
                        stmt.setString(2, titleField.getText());
                        stmt.setString(3, genreField.getText());
                        stmt.setString(4, languageField.getText());
                        stmt.setInt(5, Integer.parseInt(durationField.getText()));
                        stmt.setDate(6, Date.valueOf(dateField.getText()));
                        stmt.setBigDecimal(7, new java.math.BigDecimal(ratingField.getText()));
                        stmt.setString(8, directorField.getText());
                        stmt.setString(9, castField.getText());
                        stmt.setString(10, descriptionField.getText());
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Movie inserted successfully!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateMovie() {
        String movieId = JOptionPane.showInputDialog(this, "Enter Movie ID to Update:");

        if (movieId != null && !movieId.trim().isEmpty()) {
            JTextField titleField = new JTextField();
            JTextField genreField = new JTextField();
            JTextField languageField = new JTextField();
            JTextField durationField = new JTextField();
            JTextField dateField = new JTextField();
            JTextField ratingField = new JTextField();
            JTextField directorField = new JTextField();
            JTextField castField = new JTextField();
            JTextArea descriptionField = new JTextArea(3, 20);

            JPanel panel = new JPanel(new GridLayout(11, 2));
            panel.add(new JLabel("Movie ID:"));
            panel.add(new JLabel(movieId)); // Display movie ID here
            panel.add(new JLabel("Title:"));
            panel.add(titleField);
            panel.add(new JLabel("Genre:"));
            panel.add(genreField);
            panel.add(new JLabel("Language:"));
            panel.add(languageField);
            panel.add(new JLabel("Duration (minutes):"));
            panel.add(durationField);
            panel.add(new JLabel("Release Date (YYYY-MM-DD):"));
            panel.add(dateField);
            panel.add(new JLabel("Rating:"));
            panel.add(ratingField);
            panel.add(new JLabel("Director:"));
            panel.add(directorField);
            panel.add(new JLabel("Cast:"));
            panel.add(castField);
            panel.add(new JLabel("Description:"));
            panel.add(new JScrollPane(descriptionField));

            int result = JOptionPane.showConfirmDialog(this, panel, "Enter Movie Details to Update", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
                    String query = "UPDATE movies SET title = ?, genre = ?, language = ?, duration = ?, release_date = ?, rating = ?, director = ?, cast = ?, description = ? WHERE movie_id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, titleField.getText());
                        stmt.setString(2, genreField.getText());
                        stmt.setString(3, languageField.getText());
                        stmt.setInt(4, Integer.parseInt(durationField.getText()));
                        stmt.setDate(5, Date.valueOf(dateField.getText()));
                        stmt.setBigDecimal(6, new java.math.BigDecimal(ratingField.getText()));
                        stmt.setString(7, directorField.getText());
                        stmt.setString(8, castField.getText());
                        stmt.setString(9, descriptionField.getText());
                        stmt.setInt(10, Integer.parseInt(movieId)); // Use the provided movie_id for update
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Movie updated successfully!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteMovie() {
        String movieId = JOptionPane.showInputDialog(this, "Enter Movie ID to Delete:");

        if (movieId != null && !movieId.trim().isEmpty()) {
            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
                String query = "DELETE FROM movies WHERE movie_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, Integer.parseInt(movieId)); // Use movie_id to delete
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Movie deleted successfully!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayMovies() {
        String movieId = JOptionPane.showInputDialog(this, "Enter Movie ID to Display:");

        if (movieId != null && !movieId.trim().isEmpty()) {
            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
                String query = "SELECT * FROM movies WHERE movie_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, Integer.parseInt(movieId)); // Use movie_id to fetch the movie

                    try (ResultSet rs = stmt.executeQuery()) {
                        // Set up table model for displaying data
                        tableModel.setRowCount(0);
                        tableModel.setColumnCount(0);

                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();

                        // Add columns to table model
                        for (int i = 1; i <= columnCount; i++) {
                            tableModel.addColumn(rsmd.getColumnName(i));
                        }

                        // Add rows to table model
                        if (rs.next()) {
                            Object[] row = new Object[columnCount];
                            for (int i = 1; i <= columnCount; i++) {
                                row[i - 1] = rs.getObject(i);
                            }
                            tableModel.addRow(row);
                        } else {
                            JOptionPane.showMessageDialog(this, "No movie found with that ID.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieManagementFrame::new);
    }
}
