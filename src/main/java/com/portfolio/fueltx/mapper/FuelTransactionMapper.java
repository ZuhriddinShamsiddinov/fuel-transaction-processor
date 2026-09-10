package com.portfolio.fueltx.mapper;

import com.portfolio.fueltx.model.dto.FuelTransactionResponse;
import com.portfolio.fueltx.model.dto.FuelTransactionWebhookRequest;
import com.portfolio.fueltx.model.entity.FuelTransaction;
import com.portfolio.fueltx.model.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.mapper
 * @since 2026-09-10T12:20:00
 */
@Component
public class FuelTransactionMapper {

    public FuelTransaction toNewEntity(FuelTransactionWebhookRequest request, TransactionStatus status) {
        Instant now = Instant.now();
        return FuelTransaction.builder()
                .id(UUID.randomUUID())
                .externalTransactionId(request.getExternalTransactionId())
                .driverId(request.getDriverId())
                .vehicleId(request.getVehicleId())
                .cardNumberMasked(request.getCardNumberMasked())
                .merchantName(request.getMerchantName())
                .merchantLocation(request.getMerchantLocation())
                .gallons(request.getGallons())
                .pricePerGallon(request.getPricePerGallon())
                .totalAmount(request.getTotalAmount())
                .transactionTimestamp(request.getTransactionTimestamp())
                .status(status)
                .createdAt(now)
                .processedAt(now)
                .build();
    }

    public FuelTransactionResponse toResponse(FuelTransaction entity) {
        if (entity == null) {
            return null;
        }
        return new FuelTransactionResponse(
                entity.getId(),
                entity.getExternalTransactionId(),
                entity.getDriverId(),
                entity.getVehicleId(),
                entity.getCardNumberMasked(),
                entity.getMerchantName(),
                entity.getMerchantLocation(),
                entity.getGallons(),
                entity.getPricePerGallon(),
                entity.getTotalAmount(),
                entity.getTransactionTimestamp(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getProcessedAt()
        );
    }
}
