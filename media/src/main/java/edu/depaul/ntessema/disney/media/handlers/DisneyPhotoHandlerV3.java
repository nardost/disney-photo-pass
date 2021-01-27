package edu.depaul.ntessema.disney.media.handlers;

import edu.depaul.ntessema.disney.media.model.Photo;
import edu.depaul.ntessema.disney.media.service.DisneyPhotoService;
import lombok.RequiredArgsConstructor;
import org.bson.types.Binary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DisneyPhotoHandlerV3 {

    private final DisneyPhotoService photoService;

    public Mono<ServerResponse> getImage(ServerRequest request) {
        Mono<byte[]> photo = photoService.getPhoto(request.pathVariable("id"))
                .map(Photo::getImage)
                .map(Binary::getData);

        return ServerResponse.ok().contentType(MediaType.IMAGE_JPEG).body(photo, byte[].class);
    }

    public Mono<ServerResponse> getImageIds(ServerRequest request) {
        Flux<String> ids = photoService.listPhotos().map(Photo::getId).map(id -> id + "\n");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(ids, String.class);
    }

    // TODO
    public Mono<ServerResponse> getImages(ServerRequest request) {
        Flux<byte[]> photos = photoService.listPhotos().map(Photo::getImage).map(Binary::getData);
        return ServerResponse.ok().contentType(MediaType.IMAGE_JPEG).body(photos, byte[].class);
    }
}
