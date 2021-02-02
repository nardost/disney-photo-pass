package edu.depaul.ntessema.disney.media.handlers;

import edu.depaul.ntessema.disney.media.errors.HttpError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;

@Component
@Slf4j
@AllArgsConstructor
public class DisneyMediaErrorHandlers {

    public Mono<ServerResponse> handleNotFound(ServerRequest request) {
        final HttpError error = new HttpError(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                request.path(),
                "requestId",
                Timestamp.from(Instant.now()).toString());

        return ServerResponse.status(HttpStatus.NOT_FOUND).body(Mono.just(error), HttpError.class);
    }
}
