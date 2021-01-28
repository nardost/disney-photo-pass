package edu.depaul.ntessema.disney.media.handlers;

import edu.depaul.ntessema.disney.media.model.Hash;
import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.service.DisneyMediaService;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DisneyMediaHandler {

    private final DisneyMediaService photoService;

    public Mono<ServerResponse> getImage(ServerRequest request) {
        Mono<byte[]> photo = photoService.getPhoto(request.pathVariable("id"))
                .map(Photo::getImage)
                .map(Binary::getData);

        return ServerResponse.ok().contentType(MediaType.IMAGE_JPEG).body(photo, byte[].class);
    }

    public Mono<ServerResponse> listImages(ServerRequest request) {
        Flux<Hash> hashes = photoService.listPhotos().map(Hash::new);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_NDJSON).body(hashes, Hash.class);
    }

    public Mono<ServerResponse> saveImage(ServerRequest request) {

        Mono<DataBuffer> dataBuffer = request.body(BodyExtractors.toParts())
                .filter(part -> part.name().equals("file"))
                .flatMap(Part::content)
                .elementAt(0);

        return dataBuffer.flatMap(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);

            final Photo photo = new Photo(
                    UUID.randomUUID().toString(),
                    "image/jpeg",
                    new Binary(BsonBinarySubType.BINARY, bytes));

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(photoService.savePhoto(photo), Photo.class);
        });
    }
}
