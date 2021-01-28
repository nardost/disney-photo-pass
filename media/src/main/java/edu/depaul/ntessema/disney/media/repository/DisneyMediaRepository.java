package edu.depaul.ntessema.disney.media.repository;

import edu.depaul.ntessema.disney.media.model.Photo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DisneyMediaRepository extends ReactiveMongoRepository<Photo, String> {
    Flux<Photo> streamPhotosByMd5HashIsNotNull();
    Mono<Photo> findFirstByMd5Hash(String md5Hash);
    Mono<Boolean> existsByMd5Hash(String md5Hash);
}
