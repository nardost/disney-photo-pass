package edu.depaul.ntessema.disney.media.routes;

import edu.depaul.ntessema.disney.media.handlers.DisneyMediaHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DisneyMediaRoutes {

    private final DisneyMediaHandler photoHandler;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route()
                .GET("/v3/photo/list", accept(MediaType.APPLICATION_JSON), photoHandler::listImages)
                .POST("/v3/photo/save", accept(MediaType.MULTIPART_FORM_DATA), photoHandler::saveImage)
                .GET("/v3/photo/{id}", accept(MediaType.APPLICATION_JSON), photoHandler::getImage)
                .build();
    }
}
