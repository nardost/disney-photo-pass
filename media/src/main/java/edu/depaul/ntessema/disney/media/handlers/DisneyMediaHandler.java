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
import java.util.Date;
import java.util.UUID;

/**
 * Handler class for functional endpoints.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DisneyMediaHandler {

    private final DisneyMediaService photoService;

    /**
     * Retrieves a Photo.
     * @param request the request
     * @return Mono of ServerResponse that wraps the bytes of the image.
     */
    public Mono<ServerResponse> getImage(ServerRequest request) {
        Mono<byte[]> photo = photoService.getPhoto(request.pathVariable("id"))
                .map(Photo::getImage)
                .map(Binary::getData);

        return ServerResponse.ok().contentType(MediaType.IMAGE_JPEG).body(photo, byte[].class);
    }

    /**
     * Streams the id and hash of all Photos
     * @param request the request
     * @return Mono of Server response that wraps the stream of { id, md5hash } values.
     */
    public Mono<ServerResponse> listHashValues(ServerRequest request) {
        Flux<Hash> hashes = photoService.listPhotos().map(Hash::new);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_NDJSON).body(hashes, Hash.class);
    }

    /**
     * Saves a photo if it doesn't already exist in the collection.
     * Avoids duplication of images by comparing hash values.
     * @param request the request
     * @return Mono of ServerResponse that wraps { id, hash }.
     */
    public Mono<ServerResponse> saveImage(ServerRequest request) {

        final Mono<DataBuffer> dataBuffer = request.body(BodyExtractors.toParts())
                .filter(part -> part.name().equals("file"))
                .flatMap(Part::content)
                .elementAt(0);

        return dataBuffer.flatMap(buffer -> {
            final byte[] bytes = new byte[buffer.readableByteCount()];
            /*
             * Read bytes and release the buffer.
             * 1) https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/buffer/DataBuffer.html
             * 2) https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/buffer/DataBufferUtils.html#release-org.springframework.core.io.buffer.DataBuffer-
             */
            DataBufferUtils.release(buffer.read(bytes));

            final Photo photo = new Photo(
                    UUID.randomUUID().toString(),
                    "image/jpeg",
                    new Binary(BsonBinarySubType.BINARY, bytes),
                    getMD5Hash(bytes),
                    new Date());

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(photoService.savePhoto(photo).map(Hash::new), Hash.class);
        });
    }

    /**
     * Utility method that computes the md5 hash of image bytes.
     * @param bytes the array of bytes of the image
     * @return Base64 encoded md5 hash
     */
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
