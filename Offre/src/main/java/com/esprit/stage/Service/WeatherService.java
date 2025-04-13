package com.esprit.stage.Service;

import com.esprit.stage.dto.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherResponse getWeatherByCity(String city) {
        // 🔁 Nettoyer le nom de la ville avant appel à l'API
        String cleanCity = city.replace("Gouvernorat", "")
                .replace("gouvernorat", "")
                .replaceAll("\\s+", " ")
                .trim();

        String url = BASE_URL + "?q=" + cleanCity + "&appid=" + apiKey + "&units=metric&lang=fr";

        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(url, WeatherResponse.class);
        } catch (HttpClientErrorException e) {
            System.err.println("❌ Erreur météo - ville non trouvée : " + city);
            return new WeatherResponse(); // ou retourne null si tu préfères
        }
    }
}
