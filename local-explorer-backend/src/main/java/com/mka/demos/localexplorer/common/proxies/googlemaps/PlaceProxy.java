package com.mka.demos.localexplorer.common.proxies.googlemaps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@Component
@Slf4j
public class PlaceProxy {

    @Value("${google.api.maps.place.endpoint}")
    private String endpoint;

    @Value("${google.api.apikey}")
    private String apiKey;

    private RestTemplate restTemplate;

    @Autowired
    public PlaceProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<PlaceTextOutput> getPlaceIdFromAddress(String address) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(this.endpoint + "/findplacefromtext/json")
                    .queryParam("input", address)
                    .queryParam("inputtype", "textquery")
                    .queryParam("key", this.apiKey)
                    .build().toUri();

            ResponseEntity<PlaceTextOutput> response = restTemplate.getForEntity(uri, PlaceTextOutput.class);

            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            log.error("Something went wrong while calling the Google Places API ", e);
        }
        return Optional.empty();
    }

    public Optional<PlaceDetailsOutput> getPhotoRefence(String placeId) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(this.endpoint + "/details/json")
                    .queryParam("place_id", placeId)
                    .queryParam("fields", "photos")
                    .queryParam("key", this.apiKey)
                    .build().toUri();

            ResponseEntity<PlaceDetailsOutput> response = restTemplate.getForEntity(uri, PlaceDetailsOutput.class);

            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            log.error("Something went wrong while calling the Google Places Details API ", e);
        }
        return Optional.empty();
    }

    public Optional<byte[]> getPhoto(String photoReference) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(this.endpoint + "/photo")
                    .queryParam("maxwidth", "400")
                    .queryParam("photo_reference", photoReference)
                    .queryParam("key", this.apiKey)
                    .build().toUri();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));

            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(httpHeaders), byte[].class);

            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            log.error("Something went wrong while calling the Google Places Photo API ", e);
        }
        return Optional.empty();
    }
}
