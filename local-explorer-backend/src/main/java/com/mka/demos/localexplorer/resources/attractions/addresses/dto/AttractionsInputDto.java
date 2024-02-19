package com.mka.demos.localexplorer.resources.attractions.addresses.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttractionsInputDto {

    private String longitude;

    private String latitude;

    private String city;

}
