package edu.depaul.ntessema.disney.media.controller;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.service.ReactivePhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class ReactivePhotoController {

    private final ReactivePhotoService photoService;

    @Autowired
    public ReactivePhotoController(ReactivePhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping(value = "/v1/photo/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getPhoto(@PathVariable String id) {
        log.info("GET: /v1/photo/" + id);
        return Mono.just(photoService.getPhotoById(id)
                .defaultIfEmpty(Photo.INVALID)
                .share()
                .block()
                .getImage()
                .getData());
    }

    @GetMapping(value = "/v1/photos", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Photo> getPhotos() {
        return photoService.getAllPhotos();
    }

    @PostMapping("/v1/save")
    public Mono<Photo> save(@RequestParam("imageFile") MultipartFile imageFile) throws InterruptedException, ExecutionException {
        return photoService.savePhoto(imageFile);
    }
}
