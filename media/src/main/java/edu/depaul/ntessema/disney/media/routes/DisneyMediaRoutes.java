package edu.depaul.ntessema.disney.media.routes;

import edu.depaul.ntessema.disney.media.handlers.DisneyMediaRequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Bean that defines functional endpoints.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DisneyMediaRoutes {

    private final DisneyMediaRequestHandler photoHandler;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route()
                .GET("/v1/media/list", accept(MediaType.APPLICATION_JSON), photoHandler::listHashValues)
                .POST("/v1/media/save", accept(MediaType.MULTIPART_FORM_DATA), photoHandler::saveImage)
                .GET("/v1/media/{id}", accept(MediaType.APPLICATION_JSON), photoHandler::getImage)
                .build();
    }
}
