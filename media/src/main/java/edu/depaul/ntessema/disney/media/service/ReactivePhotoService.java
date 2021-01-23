package edu.depaul.ntessema.disney.media.service;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.repository.ReactivePhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class ReactivePhotoService {

    private final ReactivePhotoRepository repository;

    @Autowired
    public ReactivePhotoService(ReactivePhotoRepository repository) {
        this.repository = repository;
    }

    public Flux<Photo> getAllPhotos() {
        return repository.findAll();
    }

    public Mono<Photo> getPhotoById(String id) {
        return repository.findById(id);
    }

    public Mono<Photo> savePhoto(MultipartFile file) throws IOException {
        final String id = UUID.randomUUID().toString();
        final Binary image = new Binary(BsonBinarySubType.BINARY, file.getBytes());
        final Photo photo = new Photo(id, image);
        log.info("Saving image with id " + id);
        return repository.save(photo);
    }
}
