package movie_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class CustomerDashboard extends JFrame {

    private JFrame frame;
    private JPanel moviePanel;
    private JScrollPane scrollPane;
    private ArrayList<Movie> movieList;
int customerId;

    public CustomerDashboard(int customerId) {
        // Initialize the movie list
        movieList = new ArrayList<>();
        loadMovies();
this.customerId=customerId;

        // Create frame
        frame = new JFrame("Explore Movies");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center the frame

        // Movie panel to display all movies
        moviePanel = new JPanel();
        moviePanel.setLayout(new GridLayout(0, 1, 10, 10)); // Dynamic layout for movie list

        // Add movie details to the panel
        for (Movie movie : movieList) {
            JPanel movieDetailsPanel = new JPanel();
            movieDetailsPanel.setLayout(new FlowLayout());

            // Add movie name and rating
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.add(new JLabel("Title: " + movie.getTitle()));
            textPanel.add(new JLabel("Rating: " + movie.getRating()));

            // Add buttons
            JButton btnViewDetails = new JButton("View Details");
            btnViewDetails.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Display movie details in a dialog
                    displayMovieDetailsDialog(movie);
                }
            });

            JButton btnBookNow = new JButton("Book Now");
            btnBookNow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Pass only the movieId to BookNowPage
                    new BookNowPage(movie.getMovieId(), customerId); // Pass movieId to BookNowPage
                }
            });

            textPanel.add(btnViewDetails);
            textPanel.add(btnBookNow);

            movieDetailsPanel.add(textPanel);
            moviePanel.add(movieDetailsPanel);
        }

        // Add the scroll pane for the movie list
        scrollPane = new JScrollPane(moviePanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Set the title of the window and make it visible
        frame.setTitle("Explore Movies");
        frame.setVisible(true);
    }

    // Method to load movie data from the database
    private void loadMovies() {
        String dbURL = "jdbc:mysql://localhost:3306/mini";
        String dbUser = "root";
        String dbPassword = "Ssu@2005";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            String query = "SELECT * FROM movies";  // Adjusted to your 'movies' table name
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String language = rs.getString("language");
                int duration = rs.getInt("duration");
                String releaseDate = rs.getString("release_date");
                double rating = rs.getDouble("rating");
                String director = rs.getString("director");
                String cast = rs.getString("cast");
                String description = rs.getString("description");

                // Add movie to the list
                movieList.add(new Movie(movieId, title, genre, language, duration, releaseDate, rating, director, cast, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display selected movie details in a dialog
    private void displayMovieDetailsDialog(Movie movie) {
        // Create the movie details string
        String movieDetails = "<html><b>Title:</b> " + movie.getTitle() + "<br>" +
                              "<b>Genre:</b> " + movie.getGenre() + "<br>" +
                              "<b>Language:</b> " + movie.getLanguage() + "<br>" +
                              "<b>Duration:</b> " + movie.getDuration() + " minutes<br>" +
                              "<b>Release Date:</b> " + movie.getReleaseDate() + "<br>" +
                              "<b>Rating:</b> " + movie.getRating() + "<br>" +
                              "<b>Director:</b> " + movie.getDirector() + "<br>" +
                              "<b>Cast:</b> " + movie.getCast() + "<br>" +
                              "<b>Description:</b> " + movie.getDescription() + "</html>";

        // Show the movie details in a dialog box
        JOptionPane.showMessageDialog(frame, movieDetails, "Movie Details", JOptionPane.INFORMATION_MESSAGE);

        // Add Book Now button to the dialog
        int response = JOptionPane.showConfirmDialog(frame, "Would you like to book this movie?", "Book Now", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            // Pass movieId to the BookNowPage if the user clicks "Yes"
        	System.out.println("Sent customerID "+customerId);
            new BookNowPage(movie.getMovieId(), customerId); // Assuming BookNowPage exists and needs movieId and customerId
        }
    }

    public static void main(String[] args) {
        //new CustomerDashboard();
    }
}

class Movie {
    private int movieId;
    private String title;
    private String genre;
    private String language;
    private int duration;
    private String releaseDate;
    private double rating;
    private String director;
    private String cast;
    private String description;

    // Constructor to match the database schema
    public Movie(int movieId, String title, String genre, String language, int duration, String releaseDate, double rating, 
                 String director, String cast, String description) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.director = director;
        this.cast = cast;
        this.description = description;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public int getDuration() {
        return duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public String getDirector() {
        return director;
    }

    public String getCast() {
        return cast;
    }

    public String getDescription() {
        return description;
    }
}
