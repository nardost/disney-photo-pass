package edu.depaul.ntessema.disney.media.filters;

import org.slf4j.MDC;
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
@Order(2)
public class RequestIdInserterFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String requestId = getRequestId();
        exchange.getRequest()
                .mutate()
                .header("x-request-id", requestId)
                .build();
        /*
         * MDC - Mapped Diagnostic Context for Slf4j
         * Useful to filter log entries by requestId.
         * Make sure to send the requestId within error responses
         * so that the client can refer to failed requests by their id.
         */
        MDC.put("requestId", requestId);
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
