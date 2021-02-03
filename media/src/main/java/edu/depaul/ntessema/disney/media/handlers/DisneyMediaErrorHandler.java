package edu.depaul.ntessema.disney.media.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * A generic http error handler class.
 */
@Component
@Slf4j
@AllArgsConstructor
public class DisneyMediaErrorHandler {

    /**
     * An error handler method for all kinds of http errors.
     *
     * @param request the incoming request
     * @param status the error status code
     * @return a ServerResponse wrapping an HttpError object
     */
    public Mono<ServerResponse> handleHttpError(ServerRequest request, HttpStatus status) {
        final HttpError error = new HttpError(
                status.value(),
                status.getReasonPhrase(),
                null,
                request.path(),
                request.headers().firstHeader("x-request-id"),
                Timestamp.from(Instant.now()).toString());

        log.error(String.format("%s (%d) - %s - requestId: %s", error.getError(), error.getStatus(), error.getPath(), error.getRequestId()));
        return ServerResponse.status(status).body(Mono.just(error), HttpError.class);
    }
}
