package com.alura.literalura.service;

import com.alura.literalura.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class GutendexService {

    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "https://gutendex.com/books/";

    public GutendexService() {
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public ApiResponse searchBooksByTitle(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String url = BASE_URL + "?search=" + encodedTitle;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), ApiResponse.class);
            } else {
                System.out.println("Error al contactar con la API de Gutendex. Código HTTP: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al consumir la API o convertir los datos: " + e.getMessage());
            return null;
        }
    }
}
