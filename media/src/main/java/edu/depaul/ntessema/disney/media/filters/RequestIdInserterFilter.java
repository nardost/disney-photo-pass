package edu.depaul.ntessema.disney.media.filters;

import org.springframework.core.annotation.Order;
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
@Order(-2)
public class RequestIdInserterFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getRequest()
                .mutate()
                .header("x-request-id", getRequestId())
                .build();
        return chain.filter(exchange);
    }

    /**
     * Generate a globally unique request id.
     * This is as simple a method as it gets and could have been used inline.
     * It is factored out as method in case a more sophisticated id generation
     * logic is required.
     *
     * @return a globally unique request id
     */
    private String getRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
