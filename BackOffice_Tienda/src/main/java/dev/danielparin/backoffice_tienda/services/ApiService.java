package dev.danielparin.backoffice_tienda.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.danielparin.backoffice_tienda.adapters.LocalDateAdapter;
import dev.danielparin.backoffice_tienda.adapters.LocalDateTimeAdapter;


public class ApiService {
    private final HttpClient httpClient;
    private final String baseUrl;
    private final Gson gson;

    public ApiService(String baseUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // Registrar el adaptador
                .create();
    }



    //Manejar solicitudes GET
    public <T> T get(String endpoint, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Error en la solicitud GET: " + response.statusCode());
        }

        return gson.fromJson(response.body(), responseType);
    }

    //Manejar solicitudes POST
    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) throws Exception {
        String json = gson.toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Error en la solicitud POST: " + response.statusCode());
        }

        return gson.fromJson(response.body(), responseType);
    }

    //Manejar solicitudes PUT
    public <T> T put(String endpoint, Object requestBody, Class<T> responseType) throws Exception {
        String json = gson.toJson(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Error en la solicitud PUT: " + response.statusCode());
        }

        return gson.fromJson(response.body(), responseType);
    }

    //Manejar solicitudes DELETE
    public void delete(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Error en la solicitud DELETE: " + response.statusCode());
        }
    }
}

