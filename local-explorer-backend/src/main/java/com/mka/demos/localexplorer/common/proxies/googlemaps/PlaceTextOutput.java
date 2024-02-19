package com.mka.demos.localexplorer.common.proxies.googlemaps;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlaceTextOutput {

    private List<Candidate> candidates;

    @Data
    public static class Candidate {

        @JsonProperty("place_id")
        private String placeId;

    }
}
