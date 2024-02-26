package moviesapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesCommand {
    private static final String FAVORITES_FILE = "favoritesCommand.txt";

    private Set<Movie> favoriteMovies;

    public FavoritesCommand() {
        this.favoriteMovies = loadFavoritesFromFile();
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

    protected Set<Movie> loadFavoritesFromFile() {
        Set<Movie> favorites = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FAVORITES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line represents a movie title
                Movie movie = new Movie();
                movie.setTitle(line);
                favorites.add(movie);
            }
        } catch (IOException e) {
            // If file not found or any IO error occurs, return an empty set
            e.printStackTrace();
        }
        return favorites;
    }
    public void addFavoriteMovie(Movie movie) {
        boolean added = favoriteMovies.add(movie);
        if (added) {
            System.out.println("Movie added to favorites: " + movie.getTitle());
            saveFavoritesToFile(); // Save changes to file
        } else {
            System.out.println("Movie is already in favorites: " + movie.getTitle());
        }
    }




    protected void saveFavoritesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FAVORITES_FILE))) {
            for (Movie movie : favoriteMovies) {
                writer.write(movie.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveFavoritesToFile(String userName, List<Movie> movies) {
        try {
            PrintWriter writer = new PrintWriter(userName + ".txt");
            for (Movie movie : movies) {
                writer.println(movie);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        }
    }
}
