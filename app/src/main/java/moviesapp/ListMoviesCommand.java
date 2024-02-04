package moviesapp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import static moviesapp.JsonFileReader.*;


@CommandLine.Command(name = "list", description = "List all movies")
public class ListMoviesCommand implements Runnable {

    private final List<Movie> allTheMovies = new ArrayList<>();
    @Override
    public void run() {

        manage();
        for (Movie movie : allTheMovies) {

            System.out.println(movie);

        }
        System.out.println("This what I modify");
        System.out.println("-HAHA---------");
    }




    public void manage(){
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
    }


}


