package com.mka.demos.localexplorer.common.proxies.openweathermap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class OpenWeatherMapProxy {

    @Value("${openweathermap.api.weather.endpoint}")
    private String endpoint;

    @Value("${openweathermap.api.apikey}")
    private String apiKey;

    private RestTemplate restTemplate;

    @Autowired
    public OpenWeatherMapProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // TODO - keep the information in cache until the end of the day when the request is for the same city
    public Optional<DailyWeatherForcastOut> getTodaysWeatherForcast(String latitude, String longitude) {
        try {
            Map<String, String> uriParams = new HashMap<>();
            uriParams.put("longitude", longitude);
            uriParams.put("latitude", latitude);
            uriParams.put("apiKey", this.apiKey);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.endpoint);
            URI uri = builder.buildAndExpand(uriParams).toUri();

            ResponseEntity<DailyWeatherForcastOut> response = restTemplate.getForEntity(uri, DailyWeatherForcastOut.class);

            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            log.error("Something went wrong while calling the weather forcast API ", e);
        }
        return Optional.empty();
    }
}
