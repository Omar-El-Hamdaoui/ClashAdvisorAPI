package moviesapp.controller;




import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.input.MouseButton;
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

    private Set<Movie> favoriteMovies = new HashSet<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeGenreMap() ;
        genreComboBox.getItems().addAll(genreNameToIdMap.keySet());
        loadMovies();


        moviesListView.setCellFactory(param -> new ListCell<Movie>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Initialize the container HBox with spacing and padding
                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setPadding(new Insets(5, 10, 5, 10));

                    // Create a VBox for text elements
                    VBox vBoxText = new VBox(5);
                    Label titleLabel = new Label(movie.getTitle());
                    Label yearLabel = new Label(movie.getReleaseDate().substring(0, 4));
                    vBoxText.getChildren().addAll(titleLabel, yearLabel);

                    // Generate the stars rating box
                    HBox starsBox = createStarsBox(movie.getVoteAverage());

                    // Initialize the like button and set its action
                    Button likeButton = new Button();
                    likeButton.setOnAction(event -> {
                        toggleFavorite(movie);
                        // Immediately update the button text based on the new favorite status
                        likeButton.setText(favoriteMovies.contains(movie) ? "Unlike" : "Like");
                        // This refresh call might be redundant if the like button's text is already updated
                        moviesListView.refresh();
                    });

                    // Set the initial text of the like button based on whether the movie is a favorite
                    likeButton.setText(favoriteMovies.contains(movie) ? "Unlike" : "Like");
                    setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                            handleMovieClick();
                        }
                    });

                    // Apply styles and set preferred widths for better UI consistency
                    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                    yearLabel.setStyle("-fx-font-size: 12px;");
                    titleLabel.setPrefWidth(200);
                    yearLabel.setPrefWidth(200);
                    starsBox.setPrefWidth(100);

                    // Assemble the cell's graphic layout
                    hBox.getChildren().addAll(vBoxText, starsBox, likeButton);
                    setGraphic(hBox);
                }
            }

        });

    };



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

    public static List<Movie> getAllTheMovies() {
        List<Movie> allTheMovies = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File("src/text.json"));
            JsonNode resultsNode = jsonNode.get("results");

            for (JsonNode result : resultsNode) {
                Movie movie = objectMapper.treeToValue(result, Movie.class);
                allTheMovies.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allTheMovies;
    }



    private Map<String, Integer> genreNameToIdMap;

    private void initializeGenreMap() {
        genreNameToIdMap = new HashMap<>();
        genreNameToIdMap.put("Action", 28);
        genreNameToIdMap.put("Adventure", 12);
        genreNameToIdMap.put("Animation", 16);
        genreNameToIdMap.put("Comedy", 35);
        genreNameToIdMap.put("Crime", 80);
        genreNameToIdMap.put("Documentary", 99);
        genreNameToIdMap.put("Drama", 18);
        genreNameToIdMap.put("Family", 10751);
        genreNameToIdMap.put("Fantasy", 14);
        genreNameToIdMap.put("History", 36);
        genreNameToIdMap.put("Horror", 27);
        genreNameToIdMap.put("Music", 10402);
        genreNameToIdMap.put("Mystery", 9648);
        genreNameToIdMap.put("Romance", 10749);
        genreNameToIdMap.put("Science fiction", 878);
        genreNameToIdMap.put("Tv movie", 10770);
        genreNameToIdMap.put("Thriller", 53);
        genreNameToIdMap.put("War", 10752);
        genreNameToIdMap.put("Western", 37);
    }

    private Integer getGenreIdByName(String genreName) {
        if (genreName == null || genreNameToIdMap == null) {
            return null;
        }
        return genreNameToIdMap.get(genreName);
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
        if (favoriteMovies.contains(movie)) {
            favoriteMovies.remove(movie); // Supprimer le film s'il est déjà dans les favoris
        } else {
            boolean isAlreadyFavorite = favoriteMovies.stream()
                    .anyMatch(favorite -> favorite.getId() == movie.getId());
            if (!isAlreadyFavorite) {
                favoriteMovies.add(movie); // Ajouter le film s'il n'est pas déjà dans les favoris
            }
        }
        moviesListView.refresh(); // Rafraîchir la ListView pour refléter le changement
    }


    @FXML
    private void handleFavoritesButtonAction() {
        moviesListView.getItems().setAll(favoriteMovies);
    }


    @FXML
    private void handleShowAllMoviesAction() {
        loadMovies(); // Load all movies back into the list view
    }
    @FXML
    private void restart(){
        favoriteMovies.clear();
        loadMovies();
    }
    @FXML
    private void handleMovieClick() {
        Movie selectedMovie = moviesListView.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            // Afficher toutes les informations du film (par exemple, dans une boîte de dialogue)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Details du film");
            alert.setHeaderText(selectedMovie.getTitle());
            alert.setContentText(
            "Adult: " + selectedMovie.isAdult() +'\n'+
                    "Id: " + selectedMovie.getId() + ","+'\n'+
                    "Original Language: '" + selectedMovie.getOriginalLanguage() + "'"+'\n'+
                    "Original Title: '" + selectedMovie.getOriginalTitle() + "'"+'\n'+
                    "Overview: '" + selectedMovie.getOverview() + "'"+'\n'+
                    "Popularity: " + selectedMovie.getPopularity() +'\n'+
                    "Release Date: '" + selectedMovie.getReleaseDate() + "'"+'\n'+
                    "Video: " + selectedMovie.isVideo() +'\n'+
                    "Vote Average: " + selectedMovie.getVoteAverage() +'\n'+
                    "Vote Count: " + selectedMovie.getVoteCount());
            alert.showAndWait();
        }
    }



}
