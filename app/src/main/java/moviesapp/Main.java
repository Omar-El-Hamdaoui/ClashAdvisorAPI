package moviesapp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        try {
            int currentPage = 1;
            int totalPages = getTotalPages(currentPage);
            while (currentPage <= totalPages) {
                fetchAndProcessData(currentPage);
                currentPage++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getTotalPages(int page) throws Exception {
        String jsonResponse = sendHttpRequest("https://api.themoviedb.org/3/tv/top_rated?language=en-US&page=" + page);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonResponse);
        return ((Long) jsonObject.get("total_pages")).intValue();
    }

    public static void fetchAndProcessData(int page) throws Exception {
        String jsonResponse = sendHttpRequest("https://api.themoviedb.org/3/tv/top_rated?language=en-US&page=" + page);
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonResponse);
        JSONArray results = (JSONArray) jsonObject.get("results");

        for (Object obj : results) {
            JSONObject tvShow = (JSONObject) obj;
            String name = (String) tvShow.get("name");
            System.out.println("Nom de l'émission de télévision: " + name);
        }
    }

    public static String sendHttpRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            conn.disconnect();
            return response.toString();
        } else {
            System.out.println("Erreur de requête HTTP: " + conn.getResponseCode());
            conn.disconnect();
            return null;
        }
    }
}
