package moviesapp.controller;




import moviesapp.ListMoviesCommand;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import moviesapp.Movie;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static moviesapp.ListMoviesCommand.getAllTheMovies;

public class AppController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField fromYearField;
    @FXML
    private TextField toYearField;
    @FXML
    private ComboBox<String> genreComboBox;
    @FXML
    private TextField ratingField;
    @FXML
    private Button searchButton;
    @FXML
    private Button favoritesButton;
    @FXML
    private ListView<Movie> moviesListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGenreMap();
        loadMovies();


        moviesListView.setCellFactory(param -> new ListCell<Movie>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setPadding(new Insets(5, 10, 5, 10));

                    VBox vBoxText = new VBox(5);
                    Label titleLabel = new Label(movie.getTitle());
                    Label yearLabel = new Label(String.valueOf(movie.getReleaseDate().substring(0, 4))); // assuming release date is in a format that includes the year at the beginning
                    vBoxText.getChildren().addAll(titleLabel, yearLabel);

                    HBox starsBox = createStarsBox(movie.getVoteAverage());

                    Button likeButton = new Button("like");
                    likeButton.setOnAction(event -> toggleFavorite(movie));

                    // Adjust the styling of the title and year label to match your sketch
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                    yearLabel.setStyle("-fx-font-size: 12px;");

                    // Set the preferred width for the labels and the stars box to align them as in the sketch
                    titleLabel.setPrefWidth(200);
                    yearLabel.setPrefWidth(200);
                    starsBox.setPrefWidth(100);

                    hBox.getChildren().addAll(vBoxText, starsBox, likeButton);
                    setGraphic(hBox);
                }
            }
        });

    };


    private HBox createRatingBox(double rating) {
        HBox ratingBox = new HBox();
        for (int i = 0; i < 5; i++) {
            Label starLabel = new Label(i < rating ? "★" : "☆");
            ratingBox.getChildren().add(starLabel);
        }
        return ratingBox;
    }

    /*private void loadMovies() {
        // Example logic to load movies
        List<Movie> movies = getAllTheMovies();
        moviesListView.getItems().setAll(movies);
    }*/

    @FXML
    private void handleSearchButtonAction() {
        String name = nameField.getText();
        String fromYear = fromYearField.getText();
        String toYear = toYearField.getText();
        String genre = genreComboBox.getValue();
        String rating = ratingField.getText();

        List<Movie> filteredMovies = searchMovies(name, fromYear, toYear, genre, rating); // You need to implement this method.
        moviesListView.getItems().setAll(filteredMovies);
    }

    private List<Movie> searchMovies(String name, String fromYear, String toYear, String genreName, String ratingString) {
        double parsedRating = 0;
        if (ratingString != null && !ratingString.isEmpty()) {
            try {
                parsedRating = Double.parseDouble(ratingString);
            } catch (NumberFormatException e) {
                // Handle the case where the rating is not a valid double
            }
        }
        final double rating = parsedRating;

        Integer parsedGenreId = null;
        if (genreName != null && !genreName.isEmpty()) {
            parsedGenreId = getGenreIdByName(genreName);
        }
        final Integer genreId = parsedGenreId;

        List<Movie> allMovies = getAllTheMovies();

        return allMovies.stream()
                .filter(movie -> name == null || name.isEmpty() || movie.getTitle().toLowerCase().contains(name.toLowerCase()))
                .filter(movie -> genreId == null || Arrays.stream(movie.getGenreIds()).anyMatch(id -> id == genreId))
                .filter(movie -> rating == 0 || movie.getVoteAverage() >= rating)
                .filter(movie -> {
                    if (fromYear != null && !fromYear.isEmpty() && toYear != null && !toYear.isEmpty()) {
                        int year = extractYear(movie.getReleaseDate());
                        int from = Integer.parseInt(fromYear);
                        int to = Integer.parseInt(toYear);
                        return year >= from && year <= to;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    private void loadMovies() {
        List<Movie> movies = getAllTheMovies(); // Retrieve the list of all movies
        moviesListView.getItems().setAll(movies); // Add movies to the ListView
    }



    private Map<String, Integer> genreNameToIdMap;

    private void initializeGenreMap() {
        genreNameToIdMap = new HashMap<>();
        genreNameToIdMap.put("action", 28);
        genreNameToIdMap.put("adventure", 12);
        genreNameToIdMap.put("animation", 16);
        genreNameToIdMap.put("comedy", 35);
        genreNameToIdMap.put("crime", 80);
        genreNameToIdMap.put("documentary", 99);
        genreNameToIdMap.put("drama", 18);
        genreNameToIdMap.put("family", 10751);
        genreNameToIdMap.put("fantasy", 14);
        genreNameToIdMap.put("history", 36);
        genreNameToIdMap.put("horror", 27);
        genreNameToIdMap.put("music", 10402);
        genreNameToIdMap.put("mystery", 9648);
        genreNameToIdMap.put("romance", 10749);
        genreNameToIdMap.put("science fiction", 878);
        genreNameToIdMap.put("tv movie", 10770);
        genreNameToIdMap.put("thriller", 53);
        genreNameToIdMap.put("war", 10752);
        genreNameToIdMap.put("western", 37);
    }

    private Integer getGenreIdByName(String genreName) {
        if (genreName == null || genreNameToIdMap == null) {
            return null;
        }
        return genreNameToIdMap.get(genreName.toLowerCase());
    }


    private int extractYear(String releaseDate) {
        // Assuming releaseDate is a String like "1999-12-31"
        if (releaseDate != null && releaseDate.length() >= 4) {
            try {
                return Integer.parseInt(releaseDate.substring(0, 4));
            } catch (NumberFormatException e) {
                // Handle the error, perhaps return a default year or log an error.
            }
        }
        return -1; // Return an invalid year if the release date is not in the expected format.
    }







    private HBox createStarsBox(double rating) {
        HBox starsBox = new HBox();
        starsBox.setSpacing(5);
        for (int i = 1; i <= 5; i++) {
            Label starLabel = new Label();
            if (i <= rating) {
                starLabel.setText("\u2605"); // Unicode solid star
            } else {
                starLabel.setText("\u2606"); // Unicode outline star
            }
            starsBox.getChildren().add(starLabel);
        }
        return starsBox;
    }




    private void toggleFavorite(Movie movie) {
        // Toggle the favorite status of the movie
        // Update the ListView if necessary
    }

   /* @FXML
    private void handleSearchButtonAction() {
        // Implement your search logic here
        // Update moviesListView with the results
    }*/

    @FXML
    private void handleFavoritesButtonAction() {
        // Filter the ListView to show only favorite movies
    }
}
