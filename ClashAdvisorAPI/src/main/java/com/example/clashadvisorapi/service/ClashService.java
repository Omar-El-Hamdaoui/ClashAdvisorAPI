package com.example.clashadvisorapi.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
public class ClashService {

    @Value("${clash.api.token}")
    private String apiToken;

    private final String BASE_URL = "https://api.clashofclans.com/v1/players/";

    public Map<String, Object> getPlayerInfo(String tag) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String encodedTag = URLEncoder.encode(tag, StandardCharsets.UTF_8);
        String url = BASE_URL + encodedTag;

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }
}

