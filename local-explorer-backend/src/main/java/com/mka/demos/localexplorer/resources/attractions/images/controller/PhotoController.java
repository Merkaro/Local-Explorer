package com.mka.demos.localexplorer.resources.attractions.images.controller;

import com.mka.demos.localexplorer.resources.attractions.images.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/attraction/photo")
public class PhotoController {

    private PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping

    public ResponseEntity<byte[]> getAttractionPhoto(
           @RequestParam("address") String address) {
       if(photoService.getAttractionPhoto(address).isPresent()) {
           return ResponseEntity.ok()
                   .contentType(MediaType.IMAGE_JPEG)
                   .body(photoService.getAttractionPhoto(address).get());
       }
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
