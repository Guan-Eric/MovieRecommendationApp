package com.example.MovieRecommendationBackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {

    @Value("${openai.apiKey}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateMovieList() {
        String prompt = "Generate a list of movies with the following attributes: \n" +
                "Name, Year, Description, Poster URL, Trailer URL, Genre.";
        String endpoint = "https://api.openai.com/v1/completions";
        String requestData = "{\"prompt\": \"" + prompt + "\", \"max_tokens\": 200}";

        try {
            return restTemplate.postForObject(endpoint, requestData, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}