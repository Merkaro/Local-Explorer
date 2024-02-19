package com.mka.demos.localexplorer.resources.attractions.addresses.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AttractionsOutputDto {

    private List<Attraction> attractions;

    @Data
    public static class Attraction {

        private String name;

        private String description;

        private String address;

    }

}
