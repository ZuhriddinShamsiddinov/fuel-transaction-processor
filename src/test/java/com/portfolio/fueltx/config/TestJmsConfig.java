package com.portfolio.fueltx.config;

import com.portfolio.fueltx.jms.FuelTransactionListener;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:45:00
 */
@TestConfiguration
public class TestJmsConfig {

    @Bean
    @Primary
    public JmsTemplate jmsTemplate(FuelTransactionListener listener) {
        JmsTemplate template = Mockito.mock(JmsTemplate.class);
        doAnswer(invocation -> {
            FuelTransactionWebhookRequest request = invocation.getArgument(1);
            CompletableFuture.runAsync(() -> listener.onMessage(request)).join();
            return null;
        }).when(template).convertAndSend(anyString(), any(FuelTransactionWebhookRequest.class));
        return template;
    }
}
