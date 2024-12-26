package com.bensiebert.codeup.cws.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {

    public static JsonNode request(String url, String method, String body, String token) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-JWT", token)
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonNode get(String url, String token) {
        return request(url, "GET", "", token);
    }

    public static JsonNode post(String url, String body, String token) {
        return request(url, "POST", body, token);
    }

}
