package com.portfolio.fueltx.websocket;

import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.websocket
 * @since 2026-09-10T12:55:00
 */
public interface TransactionEventHub {

    /**
     * Broadcasts a transaction event to all connected WebSocket clients.
     */
    void publish(FuelTransactionResponse response);

    /**
     * Handles an inbound WebSocket session by streaming live transaction events.
     */
    Mono<Void> handleSession(WebSocketSession session);
}
