package edu.depaul.ntessema.disney.media.handlers;

import edu.depaul.ntessema.disney.media.model.Hash;
import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.service.DisneyMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Slf4j
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

        final Mono<DataBuffer> dataBuffer = request.body(BodyExtractors.toParts())
                .filter(part -> part.name().equals("file"))
                .flatMap(Part::content)
                .elementAt(0);

        return dataBuffer.flatMap(buffer -> {
            final byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);

            final String id = UUID.randomUUID().toString();
            final String mimeType = "image/jpeg";
            final Binary binary = new Binary(BsonBinarySubType.BINARY, bytes);
            final Photo photo = new Photo(id, mimeType, binary, getMD5Hash(bytes));

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(photoService.savePhoto(photo).map(Hash::new), Hash.class);
        });
    }

    private String getMD5Hash(byte[] bytes) {
        final String HASHING_ALGORITHM = "MD5";
        try {
            final byte[] digest = MessageDigest.getInstance(HASHING_ALGORITHM).digest(bytes);
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ignored) {
            log.error("Unknown hashing algorithm: " + HASHING_ALGORITHM);
            return "";
        }
    }

}
