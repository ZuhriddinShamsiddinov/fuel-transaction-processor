package com.portfolio.fueltx.config;

import com.portfolio.fueltx.model.constants.FuelTxConstants;
import com.portfolio.fueltx.websocket.TransactionEventHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.Map;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:20:00
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping transactionWebSocketMapping(TransactionEventHub eventHub) {
        WebSocketHandler handler = eventHub::handleSession;
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(-1);
        mapping.setUrlMap(Map.of(FuelTxConstants.WS_TRANSACTIONS_PATH, handler));
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
