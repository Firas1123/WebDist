package com.esprit.stage.Controller;

import com.esprit.stage.Service.WeatherService;
import com.esprit.stage.dto.WeatherResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/with-weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}")
    public WeatherResponse getWeather(@PathVariable String city) {
        return weatherService.getWeatherByCity(city);
    }
}
