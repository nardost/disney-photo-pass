package edu.depaul.ntessema.disney.media.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * A WebFilter that inserts an auto-generated request id header into the request.
 */

@Component
public class RequestIdInserterFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getRequest()
                .mutate()
                .header("x-request-id", getRequestId())
                .build();
        return chain.filter(exchange);
    }

    private String getRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
