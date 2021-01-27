package edu.depaul.ntessema.disney.media.controller;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.service.DisneyPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DisneyPhotoControllerV1 {

    private final DisneyPhotoService photoService;

    @GetMapping(value = "/v1/photo/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<ResponseEntity<byte[]>> getPhoto(@PathVariable String id) {
        return photoService.getPhoto(id)
                .map(photo -> ResponseEntity.ok().body(photo.getImage().getData()))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/v1/photo/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<byte[]> getPhotos() {
        return photoService.listPhotos()
                .map(photo -> photo.getImage().getData());
    }

    @PostMapping("/v1/photo/save")
    public Mono<Photo> save(@RequestParam("file") MultipartFile imageFile) throws InterruptedException, ExecutionException {
        return photoService.savePhoto(imageFile);
                //.map(Photo::getId);
                //.map(id -> ResponseEntity.ok().body(id))
                //.defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(""));
    }
}
