package com.portfolio.fueltx.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.websocket
 * @since 2026-09-10T12:55:00
 */
@Component
public class TransactionEventHubImpl implements TransactionEventHub {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventHubImpl.class);

    private final Sinks.Many<FuelTransactionResponse> sink =
            Sinks.many().multicast().onBackpressureBuffer();
    private final ObjectMapper objectMapper;

    public TransactionEventHubImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(FuelTransactionResponse response) {
        Sinks.EmitResult result = sink.tryEmitNext(response);
        if (result.isFailure()) {
            log.debug("WebSocket emit skipped result={} id={}", result, response.getId());
        }
    }

    @Override
    public Mono<Void> handleSession(WebSocketSession session) {
        Flux<WebSocketMessage> outbound = sink.asFlux()
                .map(event -> session.textMessage(toJson(event)))
                .doOnError(ex -> log.debug("WebSocket outbound error session={}: {}",
                        session.getId(), ex.getMessage()));

        return session.send(outbound)
                .and(session.receive().then());
    }

    private String toJson(FuelTransactionResponse response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Unable to serialize transaction event", ex);
        }
    }
}
