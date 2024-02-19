package com.mka.demos.localexplorer.resources.attractions.addresses.controller;

import com.mka.demos.localexplorer.resources.attractions.addresses.service.AttractionsService;
import com.mka.demos.localexplorer.resources.attractions.addresses.dto.AttractionsOutputDto;
import com.mka.demos.localexplorer.resources.attractions.addresses.dto.AttractionsInputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/attractions")
public class AttractionsController {

    private AttractionsService attractionsService;

    @Autowired
    public AttractionsController(AttractionsService attractionsService) {
        this.attractionsService = attractionsService;
    }

    @GetMapping
    public ResponseEntity<AttractionsOutputDto> getAttractionsToExplor(AttractionsInputDto input) {

        return ResponseEntity.of(attractionsService.getAttractionsToExplore(input));
    }


}
