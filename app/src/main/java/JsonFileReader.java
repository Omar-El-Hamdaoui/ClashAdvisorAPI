import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {
    public static void main(String[] args) {
        // Remplacez "chemin/vers/votre/fichier.json" par le chemin réel de votre fichier
        String filePath = "app/src/main/java/text.json";

        try {
            // Créez un objet ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(new File(filePath));

            // Accédez aux données spécifiques à partir du nœud JSON
            int page = jsonNode.get("page").asInt();
            int totalResults = jsonNode.get("total_results").asInt();

            System.out.println("Page: " + page);
            System.out.println("Total Results: " + totalResults);

            // Accédez aux résultats individuels
            JsonNode resultsNode = jsonNode.get("results");
            for (JsonNode result : resultsNode) {
                Movie movie = objectMapper.treeToValue(result, Movie.class);

                // Utilisez maintenant l'objet Movie
                System.out.println("Title: " + movie.getTitle());
                System.out.println("Popularity: " + movie.getPopularity());
                System.out.println("---");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
