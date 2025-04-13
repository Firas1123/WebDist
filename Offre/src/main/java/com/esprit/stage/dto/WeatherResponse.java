package com.esprit.stage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {
    private Main main;
    private Weather[] weather;
    private String name;

    @Getter
    @Setter
    public static class Main {
        private double temp;
    }

    @Getter
    @Setter
    public static class Weather {
        private String description;
        private String icon;
    }
}
