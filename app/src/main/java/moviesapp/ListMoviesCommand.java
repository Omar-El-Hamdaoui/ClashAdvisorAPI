package moviesapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "AllMovies", description = "List all movies")
public class ListMoviesCommand implements Runnable {

    @CommandLine.Option(names = {"--title"}, description = "Search by title")
    private String title;
    @CommandLine.Option(names = {"--partialTitle"}, description = "Search by partial title")
    private String partialTitle;

    @CommandLine.Option(names = {"--voteAverage"}, description = "Search by exact vote average")
    private Double voteAverage;

    @CommandLine.Option(names = {"--minVoteAverage"}, description = "Search by minimum vote average")
    private Double minVoteAverage;

    @CommandLine.Option(names = {"--maxVoteAverage"}, description = "Search by maximum vote average")
    private Double maxVoteAverage;

    @CommandLine.Option(names = {"--genreIds"}, description = "Search by genre IDs")
    private List<Integer> genreIds;

    @CommandLine.Option(names = {"--releaseDate"}, description = "Search by release date")
    private String releaseDate;

    @CommandLine.Option(names = {"--releaseDateAfter"}, description = "Search for movies released after a certain date")
    private String releaseDateAfter;

    @CommandLine.Option(names = {"--releaseDateBefore"}, description = "Search for movies released before a certain date")
    private String releaseDateBefore;
    @CommandLine.Option(names = {"--outputFile"}, description = "Specify the output file for results")
    private String outputFile;
    @CommandLine.Option(names = {"--allDetails"}, description = "Return all the details of the Movie")
    private String allDetails;

    @Override
    public void run() {
        List<Movie> allTheMovies = getAllTheMovies();

        // Filter movies based on user-specified criteria
        List<Movie> filteredMovies = allTheMovies.stream()
                .filter(movie -> (title == null || movie.getTitle().contains(title))
                        && (partialTitle == null || movie.getTitle().contains(partialTitle))
                        && (voteAverage == null || movie.getVoteAverage() == voteAverage)
                        && (minVoteAverage == null || movie.getVoteAverage() >= minVoteAverage)
                        && (maxVoteAverage == null || movie.getVoteAverage() <= maxVoteAverage)
                        && (genreIds == null || movie.getGenreIds() != null && movie.getGenreIds().length > 0
                        && movieContainsAnyGenre(movie, genreIds))
                        && (releaseDate == null || movie.getReleaseDate().contains(releaseDate))
                        && (releaseDateAfter == null || movie.getReleaseDate().compareTo(releaseDateAfter) >= 0)
                        && (releaseDateBefore == null || movie.getReleaseDate().compareTo(releaseDateBefore) <= 0))
                .collect(Collectors.toList());

        // Print the filtered movies or save to a file
        if (outputFile != null) {
            saveResultsToFile(filteredMovies, outputFile);}
       else if (allDetails !=null) {
                printResultsToConsole(filteredMovies);
            } else {
                printResultsTitles(filteredMovies);
            }
        }


    private void saveResultsToFile(List<Movie> movies, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Movie movie : movies) {
                writer.write(movie.toString());
                writer.write(System.lineSeparator());
            }
            System.out.println("Results saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printResultsToConsole(List<Movie> movies) {
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
    private void printResultsTitles(List<Movie> movies){
        for(Movie movie :movies){
            System.out.println(movie.getTitle());
        }
    }

    private boolean movieContainsAnyGenre(Movie movie, List<Integer> genreIdsToSearch) {
        for (int genreId : genreIdsToSearch) {
            for (int movieGenreId : movie.getGenreIds()) {
                if (genreId == movieGenreId) {
                    return true;
                }
            }
        }
        return false;
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
}