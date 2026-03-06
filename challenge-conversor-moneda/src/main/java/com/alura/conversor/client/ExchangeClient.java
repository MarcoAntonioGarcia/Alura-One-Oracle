package com.alura.conversor.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Encargada unicamente de construir la peticion HTTP y manejar la respuesta
 */
public class ExchangeClient {

    private static final String API_KEY = "a7ac0a691380948285eda3ca"; // Put your API key here
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    private final HttpClient client;

    public ExchangeClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Hace la peticion a la API y devuelve el JSON crudo en formato String
     *
     * @param baseCurrency   Moneda base (ej. USD)
     * @param targetCurrency Moneda objetivo (ej. ARS)
     * @return El JSON de la respuesta como String
     */
    public String fetchConversionData(String baseCurrency, String targetCurrency) {
        String uri = BASE_URL + API_KEY + "/pair/" + baseCurrency + "/" + targetCurrency;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error en la API. Código de estado: " + response.statusCode());
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Ocurrió un error al comunicarse con la API de ExchangeRate: " + e.getMessage());
        }
    }
}
