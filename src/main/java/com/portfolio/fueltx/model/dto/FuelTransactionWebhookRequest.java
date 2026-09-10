package com.portfolio.fueltx.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuelTransactionWebhookRequest {

    @NotBlank
    private String externalTransactionId;

    @NotBlank
    private String driverId;

    @NotBlank
    private String vehicleId;

    @NotBlank
    private String cardNumberMasked;

    @NotBlank
    private String merchantName;

    @NotBlank
    private String merchantLocation;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal gallons;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerGallon;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal totalAmount;

    @NotNull
    private Instant transactionTimestamp;
}
