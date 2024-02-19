package com.mka.demos.localexplorer.resources.attractions.images.service;

import com.mka.demos.localexplorer.common.proxies.googlemaps.PlaceProxy;
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
}
