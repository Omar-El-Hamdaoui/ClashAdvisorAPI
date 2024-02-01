package moviesapp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import picocli.CommandLine;



@CommandLine.Command(name = "list", description = "List all movies")
public class ListMoviesCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("This what I modify");

    }
}
