package edu.depaul.ntessema.disney.media.routes;

import edu.depaul.ntessema.disney.media.handlers.DisneyPhotoHandlerV2;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class DisneyPhotoRoutesV2 {

    private final DisneyPhotoHandlerV2 photoHandler;

    @Bean
    public RouterFunction<ServerResponse> getMediaRoutes() {
        return
                route(
                        GET("/v2/photo/{id}"),
                        request -> ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .body(photoHandler.getPhoto(request.pathVariable("id")), byte[].class))
                .andRoute(
                        GET("/v2/photo/ids"),
                        request -> ok()
                                .contentType(MediaType.TEXT_EVENT_STREAM)
                                .body(photoHandler.getAllIds(), String.class))
                .andRoute(
                        GET("/v2/photo/all"),
                        request -> ok().body(photoHandler.getAllPhotos(), byte[].class))
                .andRoute(
                        POST("/v2/photo/save"),
                        request -> ok().body(photoHandler.savePhoto(request.bodyToFlux(Part.class)), String.class));
    }
}
