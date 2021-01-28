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
        return repository.streamPhotosByMd5HashIsNotNull().delayElements(Duration.ofSeconds(1));
    }

    public Mono<Photo> getPhoto(String id) {
        return repository.findById(id);
    }

    /**
     * - If the md5 hash of the image does not exist in the collection, saves
     *   the photo and returns Mono of the newly saved photo.
     *   not exist in the collection and returns Mono of the newly saved photo.
     * - If the md5 hash already exists in the collection, returns Mono of the
     *   existing photo.
     *
     * @param photo Photo to be saved
     * @return Mono of Photo saved (or retrieved in case it already exists)
     */
    public Mono<Photo> savePhoto(Photo photo) {
        return repository.existsByMd5Hash(photo.getMd5Hash())
                .flatMap(exists -> 
                        !exists ? repository.save(photo)
                                : repository.findFirstByMd5Hash(photo.getMd5Hash()));
    }
}
