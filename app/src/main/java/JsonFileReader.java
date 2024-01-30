import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {
    public static void main(String[] args) {
        // Remplacez "chemin/vers/votre/fichier.json" par le chemin réel de votre fichier
        String filePath = "chemin/vers/votre/fichier.json";

        try {
            // Créez un objet ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Utilisez l'objet ObjectMapper pour lire le fichier JSON et créer un nœud JSON
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));

            // Accédez aux données spécifiques à partir du nœud JSON
            int page = jsonNode.get("page").asInt();
            int totalResults = jsonNode.get("total_results").asInt();

            System.out.println("Page: " + page);
            System.out.println("Total Results: " + totalResults);

            // Accédez aux résultats individuels
            JsonNode resultsNode = jsonNode.get("results");
            for (JsonNode result : resultsNode) {
                String title = result.get("title").asText();
                double popularity = result.get("popularity").asDouble();

                System.out.println("Title: " + title);
                System.out.println("Popularity: " + popularity);
                System.out.println("---");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

