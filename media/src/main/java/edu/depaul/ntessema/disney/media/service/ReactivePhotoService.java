package edu.depaul.ntessema.disney.media.service;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.repository.ReactivePhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public void save(Photo photo) {
        repository.save(photo);
    }
}
