package edu.depaul.ntessema.disney.media.repository;

import edu.depaul.ntessema.disney.media.model.Photo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DisneyPhotoRepository extends ReactiveMongoRepository<Photo, String> {
}
