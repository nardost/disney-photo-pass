package edu.depaul.ntessema.disney.media.service;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.repository.DisneyPhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class DisneyPhotoService {

    private final DisneyPhotoRepository repository;

    @Autowired
    public DisneyPhotoService(DisneyPhotoRepository repository) {
        this.repository = repository;
    }

    public Flux<Photo> listPhotos() {
        return repository.findAll().delayElements(Duration.ofSeconds(1));
    }

    public Mono<Photo> getPhoto(String id) {
        return repository.findById(id);
    }

    public Mono<Photo> savePhoto(MultipartFile file) throws InterruptedException, ExecutionException {
        final String id = UUID.randomUUID().toString();
        final String mimeType = file.getContentType();
        final Binary image = new Binary(BsonBinarySubType.BINARY, getBytesAsync(file).get());
        final Photo photo = new Photo(id, mimeType, image);
        log.info(String.format("id,mime-type,length: %s,%s,%d%n", id, mimeType, image.getData().length));
        return repository.save(photo);
    }

    public Mono<String> savePhoto(Photo photo) {
        return repository.save(photo).map(Photo::getId);
    }

    private CompletableFuture<byte[]> getBytesAsync(MultipartFile file) {
        return Mono.fromCallable(file::getBytes).subscribeOn(Schedulers.parallel()).toFuture();
    }
}
