package com.example.clashadvisorapi.service;


import com.example.clashadvisorapi.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;


import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;



@Service
public class ClashService {

    @Value("${clash.api.token}")
    private String apiToken;

    private final String BASE_URL = "https://api.clashofclans.com/v1/players/";

    public PlayerDto getPlayerInfo(String tag) {
        RestTemplate restTemplate = new RestTemplate();

        if (!tag.startsWith("#")) {
            tag = "#" + tag;
        }

        // ❌ PAS de URLEncoder ici
        String url = "https://api.clashofclans.com/v1/players/" + tag;

        System.out.println(">>> URL ENVOYÉE = " + url); // debug

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String encodedTag = URLEncoder.encode(tag, StandardCharsets.UTF_8);
            URI uri = new URI("https://api.clashofclans.com/v1/players/" + encodedTag);

            System.out.println("URL finale = " + uri.toString());


            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = response.getBody();

            // Parse les données du joueur
            PlayerDto player = new PlayerDto();
            player.setName((String) body.get("name"));
            player.setTownHallLevel((int) body.get("townHallLevel"));
            player.setExpLevel((int) body.get("expLevel"));
            player.setTrophies((int) body.get("trophies"));

            Map<String, Object> clan = (Map<String, Object>) body.get("clan");
            if (clan != null) {
                player.setClanName((String) clan.get("name"));
            }

            return player;

        } catch (HttpClientErrorException e) {
            System.out.println("❌ Erreur HTTP : " + e.getStatusCode());
            System.out.println("❌ Message : " + e.getResponseBodyAsString());
            throw new RuntimeException("Erreur lors de l'appel à l'API Clash", e);

        } catch (Exception e) {
            System.out.println("💥 Autre erreur : " + e.getMessage());
            throw new RuntimeException("Erreur inconnue", e);
        }


    }

}

