package moviesapp;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Test {
    public static void main(String[] args) {
        String apiUrl = "https://api.themoviedb.org/3/search/person?query=hadiza&include_adult=false&language=en-US&page=1";
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiOGY4NDRlNTg1MjM1ZDAzNDFiYTcyYmJjNzYzZWFkMiIsInN1YiI6IjY1YjkxMWQ1ZGE5ZWYyMDEzMGE1MDNjNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JcXJOWxG-sBArxpqdT_uhrZmBFfWwbx5IrfjLaH_izU";

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiOGY4NDRlNTg1MjM1ZDAzNDFiYTcyYmJjNzYzZWFkMiIsInN1YiI6IjY1YjkxMWQ1ZGE5ZWYyMDEzMGE1MDNjNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JcXJOWxG-sBArxpqdT_uhrZmBFfWwbx5IrfjLaH_izU")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

