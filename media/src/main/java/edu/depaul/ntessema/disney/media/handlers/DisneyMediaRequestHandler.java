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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
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
import java.util.Objects;
import java.util.UUID;

/**
 * Handler class for functional endpoints.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DisneyMediaRequestHandler {

    /**
     * Define names of all request/form parameters here.
     */
    private final static String FILE_FIELD_NAME = "file";

    /**
     * Spring will Autowire (Constructor injection)...
     */
    private final DisneyMediaService photoService;
    private final DisneyMediaErrorHandler errorHandler;

    /**
     * Retrieves a Photo.
     * @param request the request
     * @return Mono of ServerResponse that wraps the bytes of the image.
     */
    public Mono<ServerResponse> getImage(ServerRequest request) {

        /*
         * This won't be helpful in extracting mimeType or other Photo properties.
         * return ServerResponse.ok().contentType("image/jpeg").body(bytes, byte[].class)
         */
        return photoService.getPhoto(request.pathVariable("id"))
                .flatMap(photo -> {
                    // Extract the Photo properties & build response
                    final String mimeType = photo.getMimeType();
                    final Mono<byte[]> bytes = Mono.just(photo.getImage().getData());

                    return ServerResponse.ok()
                            .contentType(MediaType.valueOf(mimeType))
                            .body(bytes, byte[].class);
                }).switchIfEmpty(errorHandler.handleHttpError(request, HttpStatus.NOT_FOUND));
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

        /*
         * Incoming files larger than 256K are split into a flux of 1K sized
         * DataBuffer chunks. Why?
         * The series of DataBuffer chunks in the Flux have to be merged into
         * one DataBuffer with DataBufferUtils::join to get the uploaded file
         * in one piece (the Flux is transformed into a Mono).
         *
         * I made the mistake of getting the first element of the flux with elementAt(0) or next()
         * and wasted hours trying to solve why files larger than 256K were being saved as
         * 1K sized files which show as blank images when retrieved with browser/Postman. It worked
         * for files smaller than 256K as they were not split into a flux of 1K chunks.
         */
        final StringBuilder mimeType = new StringBuilder();
        final Mono<DataBuffer> dataBuffer = DataBufferUtils.join(request.body(BodyExtractors.toParts())
                .filter(part -> part instanceof FilePart)
                .filter(part -> part.name().equals(FILE_FIELD_NAME))
                .cast(FilePart.class)
                .map(filePart -> {
                    mimeType.append(Objects.requireNonNullElse(filePart.headers().getContentType(), MediaType.IMAGE_JPEG).toString());
                    return filePart;
                })
                .flatMap(FilePart::content)
        );

        return dataBuffer.flatMap(buffer -> {
            final byte[] bytes = new byte[buffer.readableByteCount()];

            // DataBuffer has to be released
            DataBufferUtils.release(buffer.read(bytes));

            final Photo photo = new Photo(
                    UUID.randomUUID().toString(),
                    mimeType.toString(),
                    new Binary(BsonBinarySubType.BINARY, bytes),
                    getMD5Hash(bytes),
                    new Date()); //TODO: Investigate why Timestamp causes problems (MongoConverter problems...)

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(photoService.savePhoto(photo).map(Hash::new), Hash.class)
                    .switchIfEmpty(errorHandler.handleHttpError(request, HttpStatus.NOT_FOUND));
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
