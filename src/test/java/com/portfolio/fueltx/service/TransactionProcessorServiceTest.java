package com.portfolio.fueltx.service;

import com.portfolio.fueltx.mapper.FuelTransactionMapper;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.model.entity.FuelTransaction;
import com.portfolio.fueltx.model.enums.TransactionStatus;
import com.portfolio.fueltx.repository.FuelTransactionRepository;
import com.portfolio.fueltx.service.impl.TransactionProcessorServiceImpl;
import com.portfolio.fueltx.websocket.TransactionEventHub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionProcessorServiceTest {

    @Mock
    private FuelTransactionRepository repository;
    @Mock
    private FraudCheckService fraudCheckService;
    @Mock
    private DriverSummaryCacheService summaryCacheService;
    @Mock
    private TransactionEventHub eventHub;

    private final FuelTransactionMapper mapper = new FuelTransactionMapper();
    private TransactionProcessorService processorService;

    @BeforeEach
    void setUp() {
        processorService = new TransactionProcessorServiceImpl(
                repository, fraudCheckService, summaryCacheService, mapper, eventHub);
    }

    @Test
    void process_skipsDuplicateWithoutSaving() {
        FuelTransactionWebhookRequest request = sampleRequest("TXN-DUP");
        when(repository.existsByExternalTransactionId("TXN-DUP")).thenReturn(Mono.just(true));

        StepVerifier.create(processorService.process(request))
                .verifyComplete();

        verify(repository, never()).save(any());
        verify(fraudCheckService, never()).shouldFlag(any());
    }

    @Test
    void process_savesProcessedAndRefreshesCache() {
        FuelTransactionWebhookRequest request = sampleRequest("TXN-OK");
        when(repository.existsByExternalTransactionId("TXN-OK")).thenReturn(Mono.just(false));
        when(fraudCheckService.shouldFlag(request)).thenReturn(Mono.just(false));
        when(repository.save(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(summaryCacheService.refreshAllPeriods("DRV-1")).thenReturn(Mono.empty());

        StepVerifier.create(processorService.process(request))
                .assertNext(saved -> assertThat(saved.getStatus()).isEqualTo(TransactionStatus.PROCESSED))
                .verifyComplete();

        ArgumentCaptor<FuelTransaction> captor = ArgumentCaptor.forClass(FuelTransaction.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getExternalTransactionId()).isEqualTo("TXN-OK");
        verify(summaryCacheService).refreshAllPeriods("DRV-1");
    }

    @Test
    void process_savesFlaggedWithoutCacheRefresh() {
        FuelTransactionWebhookRequest request = sampleRequest("TXN-FLAG");
        when(repository.existsByExternalTransactionId("TXN-FLAG")).thenReturn(Mono.just(false));
        when(fraudCheckService.shouldFlag(request)).thenReturn(Mono.just(true));
        when(repository.save(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(processorService.process(request))
                .assertNext(saved -> {
                    assertThat(saved.getStatus()).isEqualTo(TransactionStatus.FLAGGED);
                    assertThat(saved.getId()).isInstanceOf(UUID.class);
                })
                .verifyComplete();

        verify(summaryCacheService, never()).refreshAllPeriods(any());
    }

    private FuelTransactionWebhookRequest sampleRequest(String externalId) {
        return FuelTransactionWebhookRequest.builder()
                .externalTransactionId(externalId)
                .driverId("DRV-1")
                .vehicleId("VEH-1")
                .cardNumberMasked("****1234")
                .merchantName("Pilot")
                .merchantLocation("Amarillo, TX")
                .gallons(new BigDecimal("50"))
                .pricePerGallon(new BigDecimal("3.50"))
                .totalAmount(new BigDecimal("175.00"))
                .transactionTimestamp(Instant.parse("2026-09-10T14:00:00Z"))
                .build();
    }
}
