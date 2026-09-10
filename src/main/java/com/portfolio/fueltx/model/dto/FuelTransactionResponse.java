package com.portfolio.fueltx.model.dto;

import com.portfolio.fueltx.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuelTransactionResponse {

    private UUID id;
    private String externalTransactionId;
    private String driverId;
    private String vehicleId;
    private String cardNumberMasked;
    private String merchantName;
    private String merchantLocation;
    private BigDecimal gallons;
    private BigDecimal pricePerGallon;
    private BigDecimal totalAmount;
    private Instant transactionTimestamp;
    private TransactionStatus status;
    private Instant createdAt;
    private Instant processedAt;
}
