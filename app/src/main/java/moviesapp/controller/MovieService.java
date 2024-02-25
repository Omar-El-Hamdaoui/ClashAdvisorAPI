package moviesapp.controller;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import moviesapp.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class MovieService {
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executorService;
    private final String apiKey = "b8f844e585235d0341ba72bbc763ead2";

    public MovieService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Set<Movie> fetchAllMovies(int totalApiPages) {
        Set<Movie> allMovies = new HashSet<>();
        for (int page = 1; page <= totalApiPages; page++) {
            int finalPage = page;
            executorService.execute(() -> {
                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/popular?language=en-US&page=" + finalPage + "&api_key=" + apiKey)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful() && response.body() != null) {
                        String responseBody = response.body().string();
                        JsonNode rootNode = objectMapper.readTree(responseBody);
                        JsonNode resultsNode = rootNode.path("results");
                        for (JsonNode node : resultsNode) {
                            Movie movie = objectMapper.treeToValue(node, Movie.class);
                            synchronized (allMovies) {
                                allMovies.add(movie);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return allMovies; // This might not work as expected due to asynchronous execution
    }

}
