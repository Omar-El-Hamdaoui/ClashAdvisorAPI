package moviesapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@CommandLine.Command(name = "AllMovies", description = "List all movies")
public class ListMoviesCommand implements Runnable {
    private User currentUser;
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    public static void main(String[] args) {
        // Initialize your CLI application components
        ListMoviesCommand appCLI = new ListMoviesCommand();


        // Proceed with the application logic
        appCLI.run();
    }
    private String title;

    private String partialTitle;

    private Double voteAverage;

    private Double minVoteAverage;

    private Double maxVoteAverage;

    private List<Integer> genreIds;

    private String releaseDate;
    private String releaseDateAfter;
    private String releaseDateBefore;
    private String outputFile;
    private String allDetails;

    @Override
    public void run() {
        List<Movie> allTheMovies = getAllTheMovies();
        List<Movie> filteredMovies = filterMovies(allTheMovies);

        // Print the filtered movies or save to a file
        if (outputFile != null) {
            saveResultsToFile(filteredMovies, outputFile);
        } else if (allDetails != null) {
            printResultsToConsole(filteredMovies);
        } else {
            printResultsTitles(filteredMovies);
        }

        // Start interactive search if not saving to file
        if (outputFile == null) {
            interactiveSearch(filteredMovies);
        }
    }

    private void interactiveSearch(List<Movie> movies) {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            // Display current search results
            printResults(movies);

            // Ask for more details
            System.out.println("Do you want to see all details? (yes/no)");
            allDetails = scanner.nextLine().trim();
            if ("yes".equalsIgnoreCase(allDetails)) {
                printResultsToConsole(movies); // If yes, print all details
            }

            // Ask to save results to file
            System.out.println("Do you want to save the results to a file? (yes/no)");
            input = scanner.nextLine().trim();
            if ("yes".equalsIgnoreCase(input)) {
                System.out.println("Enter file name:");
                String fileName = scanner.nextLine().trim();
                saveResultsToFile(movies, fileName);
                break; // After saving, we break the loop as the user's primary action is completed
            }

            // Ask if the user wants to refine the search further
            System.out.println("Do you want to add criteria search ? (yes/no)");
            input = scanner.nextLine().trim();
            if ("no".equalsIgnoreCase(input)) {
                break; // Exit loop if user does not want to refine search further
            }

            // Refine the search based on user inputs
            System.out.println("Enter criteria for refining search (title/partialTitle/voteAverage/minVoteAverage/maxVoteAverage/genreIds/releaseDate/releaseDateAfter/releaseDateBefore):");
            String criteria = scanner.nextLine().trim();
            applyCriteria(criteria, scanner);

            movies = filterMovies(movies); // Re-filter movies based on the newly applied criteria
        } while (true);
    }

    private void applyCriteria(String criteria, Scanner scanner) {
        switch (criteria) {
            case "title":
                System.out.println("Enter title:");
                title = scanner.nextLine().trim();
                break;
            case "partialTitle":
                System.out.println("Enter partial title:");
                partialTitle = scanner.nextLine().trim();
                break;
            case "voteAverage":
                System.out.println("Enter vote average:");
                voteAverage = Double.valueOf(scanner.nextLine().trim());
                break;
            case "minVoteAverage":
                System.out.println("Enter minimum vote average:");
                minVoteAverage = Double.valueOf(scanner.nextLine().trim());
                break;
            case "maxVoteAverage":
                System.out.println("Enter maximum vote average:");
                maxVoteAverage = Double.valueOf(scanner.nextLine().trim());
                break;
            case "genreIds":
                System.out.println("Enter genre IDs (comma-separated):");
                String[] ids = scanner.nextLine().trim().split(",");
                genreIds = Arrays.stream(ids).map(Integer::valueOf).collect(Collectors.toList());
                break;
            case "releaseDate":
                System.out.println("Enter release date:");
                releaseDate = scanner.nextLine().trim();
                break;
            case "releaseDateAfter":
                System.out.println("Enter release date after:");
                releaseDateAfter = scanner.nextLine().trim();
                break;
            case "releaseDateBefore":
                System.out.println("Enter release date before:");
                releaseDateBefore = scanner.nextLine().trim();
                break;
            default:
                System.out.println("Invalid criteria. Please try again.");
                break;
        }
    }



    private void printResults(List<Movie> movies) {
        if ("full".equals(allDetails)) {
            printResultsToConsole(movies);
        } else {
            printResultsTitles(movies);
        }
    }



    private List<Movie> filterMovies(List<Movie> movies) {
        return movies.stream()
                .filter(movie -> (title == null || movie.getTitle().contains(title))
                        && (partialTitle == null || movie.getTitle().contains(partialTitle))
                        && (voteAverage == null || movie.getVoteAverage()==(voteAverage))
                        && (minVoteAverage == null || movie.getVoteAverage() >= minVoteAverage)
                        && (maxVoteAverage == null || movie.getVoteAverage() <= maxVoteAverage)
                        && (genreIds == null || movie.getGenreIds() != null && movie.getGenreIds().length > 0
                        && movieContainsAnyGenre(movie, genreIds))
                        && (releaseDate == null || movie.getReleaseDate().contains(releaseDate))
                        && (releaseDateAfter == null || movie.getReleaseDate().compareTo(releaseDateAfter) > 0)
                        && (releaseDateBefore == null || movie.getReleaseDate().compareTo(releaseDateBefore) < 0))
                .collect(Collectors.toList());
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
            JsonNode jsonNode = objectMapper.readTree(new File("app/src/text.json"));
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

