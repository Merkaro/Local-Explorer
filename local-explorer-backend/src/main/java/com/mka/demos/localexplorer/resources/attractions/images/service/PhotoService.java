package com.mka.demos.localexplorer.resources.attractions.images.service;

import com.mka.demos.localexplorer.common.proxies.googlemaps.PlaceProxy;
import com.mka.demos.localexplorer.common.proxies.googlemaps.PlaceTextOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Service
public class PhotoService {

    private PlaceProxy placeProxy;

    @Autowired
    public PhotoService(PlaceProxy placeProxy) {
        this.placeProxy = placeProxy;
    }

    public Optional<byte[]> getAttractionPhoto(String address) {
        return placeProxy.getPlaceIdFromAddress(address)
                .flatMap(placeText -> {
                    if (CollectionUtils.isEmpty(placeText.getCandidates()) || placeText.getCandidates().get(0) == null) {
                        return Optional.empty();
                    }
                    String placeId = placeText.getCandidates().get(0).getPlaceId();
                    return placeProxy.getPhotoRefence(placeId).flatMap(placeDetailsOutput -> {
                        if (placeDetailsOutput.getResult() == null ||
                                CollectionUtils.isEmpty(placeDetailsOutput.getResult().getPhotos()) ||
                                placeDetailsOutput.getResult().getPhotos().get(0) == null) {
                            return Optional.empty();
                        }
                        String photoReference = placeDetailsOutput.getResult().getPhotos().get(0).getPhotoReference();
                        return placeProxy.getPhoto(photoReference);
                    });
                });
    }

    /*public Optional<byte[]> getAttractionPhoto(String address) {

        // Places API => finding the place ID
         Optional<PlaceTextOutput> placeTextOutput = placeProxy.getPlaceIdFromAddress(address);

         if (placeTextOutput.isPresent() && !CollectionUtils.isEmpty(placeTextOutput.get().getCandidates())
                 && placeTextOutput.get().getCandidates().get(0) != null) {
              String placeId = placeTextOutput.get().getCandidates().get(0).getPlaceId();

             // Places API => finding the photoReference
             Optional<PlaceDetailsOutput> placeDetailsOutput = placeProxy.getPhotoRefence(placeId);
             if (placeDetailsOutput.isPresent() && placeDetailsOutput.get().getResult() != null
                     && !CollectionUtils.isEmpty(placeDetailsOutput.get().getResult().getPhotos())
                     && placeDetailsOutput.get().getResult().getPhotos().get(0) != null) {
                 String photoReference = placeDetailsOutput.get().getResult().getPhotos().get(0).getPhotoReference();

                 // Places API => finding the image object
                 return placeProxy.getPhoto(photoReference);
             }
         }
        return Optional.empty();
    }*/
}
