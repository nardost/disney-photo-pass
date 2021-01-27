package edu.depaul.ntessema.disney.media.handlers;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.service.DisneyPhotoService;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DisneyPhotoHandlerV2 {

    private final DisneyPhotoService photoService;

    public Mono<byte[]> getPhoto(String id) {
        return photoService.getPhoto(id).map(Photo::getImage).map(Binary::getData);
    }

    public Flux<String> getAllIds() {
        return photoService.listPhotos().map(Photo::getId);
    }

    public Flux<byte[]> getAllPhotos() {
        return photoService.listPhotos().map(Photo::getImage).map(Binary::getData);
    }

    public Mono<String> savePhoto(Flux<Part> multipartFile) {
        Flux<DataBuffer> buffer = Flux.from(multipartFile.flatMap(Part::content));
        return buffer.flatMap(buf -> {
            byte[] bytes = new byte[buf.readableByteCount()];
            buf.read(bytes);
            DataBufferUtils.release(buf);
            final String id = UUID.randomUUID().toString();
            final Binary image = new Binary(BsonBinarySubType.BINARY, bytes);
            final Photo photo = new Photo(id, "image/jpeg", image);
            return photoService.savePhoto(photo);
        }).elementAt(0);
    }
}
