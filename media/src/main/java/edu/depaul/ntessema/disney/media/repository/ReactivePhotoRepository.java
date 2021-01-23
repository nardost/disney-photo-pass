package edu.depaul.ntessema.disney.media.repository;

import edu.depaul.ntessema.disney.media.model.Photo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactivePhotoRepository extends ReactiveMongoRepository<Photo, String> {
}
