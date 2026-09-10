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
        return FuelTransactionResponse.builder()
                .id(entity.getId())
                .externalTransactionId(entity.getExternalTransactionId())
                .driverId(entity.getDriverId())
                .vehicleId(entity.getVehicleId())
                .cardNumberMasked(entity.getCardNumberMasked())
                .merchantName(entity.getMerchantName())
                .merchantLocation(entity.getMerchantLocation())
                .gallons(entity.getGallons())
                .pricePerGallon(entity.getPricePerGallon())
                .totalAmount(entity.getTotalAmount())
                .transactionTimestamp(entity.getTransactionTimestamp())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .processedAt(entity.getProcessedAt())
                .build();
    }
}
