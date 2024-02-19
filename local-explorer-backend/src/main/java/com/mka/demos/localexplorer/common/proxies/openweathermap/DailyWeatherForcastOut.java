package com.mka.demos.localexplorer.common.proxies.openweathermap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DailyWeatherForcastOut {

    private City city;
    private String cod;
    private double message;
    private int cnt;
    private List<WeatherData> list;

    @Data
    private static class City {
        private String name;
        private Coord coord;
        private String country;
        private int population;
        private int timezone;
    }

    @Data
    private static class Coord {
        private double lon;
        private double lat;
    }

    @Data
    public static class WeatherData {
        private long dt;
        private Temp temp;
        @JsonProperty("feels_like")
        private FeelsLike feelsLike;
        private int humidity;
        private List<Weather> weather;
        private double speed;
        private int deg;
        private double gust;
        private int clouds;
        private double pop;
        private double rain;
    }

    @Data
    private static class Temp {
        private double day;
        private double min;
        private double max;
        private double night;
        private double eve;
        private double morn;
    }

    @Data
    private static class FeelsLike {
        private double day;
        private double night;
        private double eve;
        private double morn;
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
    }
}