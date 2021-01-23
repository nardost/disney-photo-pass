package edu.depaul.ntessema.disney.media.controller;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.service.ReactivePhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ReactivePhotoController {

    private final ReactivePhotoService photoService;

    @Autowired
    public ReactivePhotoController(ReactivePhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping(value = "/v1/photo/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Photo> getPhoto(@PathVariable String id) {
        return photoService.getPhotoById(id).defaultIfEmpty(Photo.INVALID);
    }
}
