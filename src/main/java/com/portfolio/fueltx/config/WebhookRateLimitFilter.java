package com.portfolio.fueltx.config;

import com.portfolio.fueltx.exception.RateLimitExceededException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.reactor.ratelimiter.operator.RateLimiterOperator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:20:00
 */
@Component
public class WebhookRateLimitFilter implements WebFilter {

    public static final String CACHED_BODY_ATTR = "fueltx.cachedRequestBody";

    private static final String WEBHOOK_PATH = "/api/v1/webhooks/fuel-transactions";
    private static final String RATE_LIMITER_NAME = "webhookIngestion";

    private final RateLimiter rateLimiter;

    public WebhookRateLimitFilter(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiter = rateLimiterRegistry.rateLimiter(RATE_LIMITER_NAME);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!isWebhookPost(exchange)) {
            return chain.filter(exchange);
        }

        return DataBufferUtils.join(exchange.getRequest().getBody())
                .defaultIfEmpty(exchange.getResponse().bufferFactory().wrap(new byte[0]))
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    String body = new String(bytes, StandardCharsets.UTF_8);
                    exchange.getAttributes().put(CACHED_BODY_ATTR, body);

                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return Flux.defer(() ->
                                    Flux.just(exchange.getResponse().bufferFactory().wrap(bytes)));
                        }
                    };

                    return chain.filter(exchange.mutate().request(decorator).build())
                            .transformDeferred(RateLimiterOperator.of(rateLimiter))
                            .onErrorMap(io.github.resilience4j.ratelimiter.RequestNotPermitted.class,
                                    ex -> new RateLimitExceededException("Webhook rate limit exceeded"));
                });
    }

    private boolean isWebhookPost(ServerWebExchange exchange) {
        return HttpMethod.POST.equals(exchange.getRequest().getMethod())
                && WEBHOOK_PATH.equals(exchange.getRequest().getPath().pathWithinApplication().value());
    }
}
