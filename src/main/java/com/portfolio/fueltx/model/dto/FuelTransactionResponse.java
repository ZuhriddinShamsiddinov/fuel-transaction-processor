package com.portfolio.fueltx.model.dto;

import com.portfolio.fueltx.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
public record FuelTransactionResponse(
        UUID id,
        String externalTransactionId,
        String driverId,
        String vehicleId,
        String cardNumberMasked,
        String merchantName,
        String merchantLocation,
        BigDecimal gallons,
        BigDecimal pricePerGallon,
        BigDecimal totalAmount,
        Instant transactionTimestamp,
        TransactionStatus status,
        Instant createdAt,
        Instant processedAt
) {
}
