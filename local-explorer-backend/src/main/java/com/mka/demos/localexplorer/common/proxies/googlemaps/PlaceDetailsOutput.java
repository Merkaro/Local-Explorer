package com.mka.demos.localexplorer.common.proxies.googlemaps;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlaceDetailsOutput {

    private Result result;

    @Data
    public static class Result {

        private List<Photo> photos;

    }

    @Data
    public static class Photo {

        @JsonProperty("photo_reference")
        private String photoReference;

        @JsonProperty("html_attributions")
        private List<String> htmlAttributions;

    }
}
