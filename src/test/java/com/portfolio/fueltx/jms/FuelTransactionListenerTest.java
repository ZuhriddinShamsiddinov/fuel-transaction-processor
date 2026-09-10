package com.portfolio.fueltx.jms;

import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.model.entity.FuelTransaction;
import com.portfolio.fueltx.model.enums.TransactionStatus;
import com.portfolio.fueltx.service.TransactionProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuelTransactionListenerTest {

    @Mock
    private TransactionProcessorService processorService;

    @Test
    void onMessage_delegatesToProcessor() {
        FuelTransactionListener listener = new FuelTransactionListener(processorService);
        FuelTransactionWebhookRequest request = FuelTransactionWebhookRequest.builder()
                .externalTransactionId("TXN-JMS-1")
                .driverId("DRV-1")
                .vehicleId("VEH-1")
                .cardNumberMasked("****1234")
                .merchantName("Pilot")
                .merchantLocation("TX")
                .gallons(new BigDecimal("40"))
                .pricePerGallon(new BigDecimal("3.10"))
                .totalAmount(new BigDecimal("124.00"))
                .transactionTimestamp(Instant.parse("2026-09-10T15:00:00Z"))
                .build();

        FuelTransaction saved = FuelTransaction.builder()
                .id(UUID.randomUUID())
                .externalTransactionId("TXN-JMS-1")
                .status(TransactionStatus.PROCESSED)
                .build();

        when(processorService.process(any())).thenReturn(Mono.just(saved));

        listener.onMessage(request);

        verify(processorService).process(request);
    }
}
