package com.example.clashadvisorapi.service;

import com.example.clashadvisorapi.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

        String url = BASE_URL + tag;
        System.out.println(">>> URL ENVOYÉE = " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            String encodedTag = URLEncoder.encode(tag, StandardCharsets.UTF_8);
            URI uri = new URI(BASE_URL + encodedTag);
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = response.getBody();

            PlayerDto player = new PlayerDto();
            player.setName((String) body.get("name"));
            player.setTownHallLevel((int) body.get("townHallLevel"));
            player.setExpLevel((int) body.get("expLevel"));
            player.setTrophies((int) body.get("trophies"));
            player.setWarStars((int) body.get("warStars"));

            Map<String, Object> clan = (Map<String, Object>) body.get("clan");
            if (clan != null) {
                player.setClanName((String) clan.get("name"));
                player.setClanLevel((int) clan.get("clanLevel"));
            }

            Map<String, Object> league = (Map<String, Object>) body.get("league");
            if (league != null) {
                player.setLeagueName((String) league.get("name"));
                Map<String, Object> iconUrls = (Map<String, Object>) league.get("iconUrls");
                if (iconUrls != null) {
                    player.setLeagueIconUrl((String) iconUrls.get("small"));
                }
            }

            return player;

        } catch (HttpClientErrorException e) {
            System.out.println(" Erreur HTTP : " + e.getStatusCode());
            System.out.println(" Message : " + e.getResponseBodyAsString());
            throw new RuntimeException("Erreur lors de l'appel à l'API Clash", e);

        } catch (Exception e) {
            System.out.println("💥 Autre erreur : " + e.getMessage());
            throw new RuntimeException("Erreur inconnue", e);
        }
    }

    public Resource exportPlayerAsCsv(String tag) {
        PlayerDto player = getPlayerInfo(tag);

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Name,TownHallLevel,ExpLevel,Trophies,ClanName,ClanLevel,WarStars,League\n");
        csvBuilder.append(String.format("%s,%d,%d,%d,%s,%d,%d,%s",
                player.getName(),
                player.getTownHallLevel(),
                player.getExpLevel(),
                player.getTrophies(),
                player.getClanName() != null ? player.getClanName() : "",
                player.getClanLevel(),
                player.getWarStars(),
                player.getLeagueName() != null ? player.getLeagueName() : ""));

        byte[] csvBytes = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
        return new ByteArrayResource(csvBytes);
    }
}
