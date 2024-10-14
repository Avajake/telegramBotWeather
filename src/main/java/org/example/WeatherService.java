package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.example.ApiKey.api_weather;
import static org.example.ApiKey.url;

public class WeatherService {
    public String getfethcWeatherCity(String city) throws IOException, InterruptedException {

        ObjectMapper objectMapper = new ObjectMapper();
        String apiUrl = url + city + "&appid=" + api_weather;


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Логируем ответ
            String content = response.body();

            JsonNode rootPath = objectMapper.readTree(content);

            // Проверка на наличие данных
            if (rootPath.has("weather") && rootPath.path("weather").isArray() && rootPath.path("weather").size() > 0) {
                JsonNode weatherPath = rootPath.path("weather").get(0);
                return weatherPath.get("main").asText();
            } else {
                return "Нет данных о погоде для данного города.";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Не удалось получить данные о погоде.";
        }

    }
}
