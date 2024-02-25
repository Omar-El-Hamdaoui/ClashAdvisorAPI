package moviesapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FavoritesCommand {
    private static final String FAVORITES_FILE = "favoritesCommand.txt";

    private Set<Movie> favoriteMovies;

    public FavoritesCommand() {
        this.favoriteMovies = loadFavoritesFromFile();
    }

    public void addFavoriteMovie(Movie movie) {
        favoriteMovies.add(movie);
        saveFavoritesToFile();
        System.out.println("Movie added to favorites: " + movie.getTitle());
    }

    public void removeFavoriteMovie(Movie movie) {
        if (favoriteMovies.remove(movie)) {
            saveFavoritesToFile();
            System.out.println("Movie removed from favorites: " + movie.getTitle());
        } else {
            System.out.println("Movie not found in favorites: " + movie.getTitle());
        }
    }

    public void listFavoriteMovies() {
        if (favoriteMovies.isEmpty()) {
            System.out.println("No favorite movies.");
        } else {
            System.out.println("Favorite Movies:");
            favoriteMovies.forEach(movie -> System.out.println(movie.getTitle()));
        }
    }

    private Set<Movie> loadFavoritesFromFile() {
        Set<Movie> favorites = new HashSet<>();
        ObjectMapper objectMapper = new ObjectMapper(); // Assuming you're using Jackson ObjectMapper
        try (BufferedReader reader = new BufferedReader(new FileReader(FAVORITES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse JSON data from each line and create Movie objects
                JsonNode jsonNode = objectMapper.readTree(line);
                Movie movie = objectMapper.treeToValue(jsonNode, Movie.class);
                favorites.add(movie);
            }
        } catch (IOException e) {
            // If file not found or any IO error occurs, return an empty set
            e.printStackTrace();
        }
        return favorites;
    }


    private void saveFavoritesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITES_FILE))) {
            for (Movie movie : favoriteMovies) {
                writer.write(movie.getTitle());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
