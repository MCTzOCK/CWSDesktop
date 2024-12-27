package com.bensiebert.codeup.cws.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpUtils {

    public static JsonNode request(String url, String method, String body, String token) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("X-JWT", token)
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(10))
                .build();

        System.out.println(request);

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
