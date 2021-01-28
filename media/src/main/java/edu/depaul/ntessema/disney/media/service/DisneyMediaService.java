package edu.depaul.ntessema.disney.media.service;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.repository.DisneyMediaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class DisneyMediaService {

    private final DisneyMediaRepository repository;

    @Autowired
    public DisneyMediaService(DisneyMediaRepository repository) {
        this.repository = repository;
    }

    public Flux<Photo> listPhotos() {
        return repository.findAll().delayElements(Duration.ofSeconds(1));
    }

    public Mono<Photo> getPhoto(String id) {
        return repository.findById(id);
    }

    public Mono<Photo> savePhoto(Photo photo) {
        return repository.save(photo);
    }
}
