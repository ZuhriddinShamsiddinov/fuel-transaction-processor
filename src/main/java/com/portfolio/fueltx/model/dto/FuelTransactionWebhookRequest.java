package com.portfolio.fueltx.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
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

    public FuelTransactionWebhookRequest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCardNumberMasked() {
        return cardNumberMasked;
    }

    public void setCardNumberMasked(String cardNumberMasked) {
        this.cardNumberMasked = cardNumberMasked;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantLocation() {
        return merchantLocation;
    }

    public void setMerchantLocation(String merchantLocation) {
        this.merchantLocation = merchantLocation;
    }

    public BigDecimal getGallons() {
        return gallons;
    }

    public void setGallons(BigDecimal gallons) {
        this.gallons = gallons;
    }

    public BigDecimal getPricePerGallon() {
        return pricePerGallon;
    }

    public void setPricePerGallon(BigDecimal pricePerGallon) {
        this.pricePerGallon = pricePerGallon;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(Instant transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public static final class Builder {
        private final FuelTransactionWebhookRequest target = new FuelTransactionWebhookRequest();

        public Builder externalTransactionId(String externalTransactionId) {
            target.externalTransactionId = externalTransactionId;
            return this;
        }

        public Builder driverId(String driverId) {
            target.driverId = driverId;
            return this;
        }

        public Builder vehicleId(String vehicleId) {
            target.vehicleId = vehicleId;
            return this;
        }

        public Builder cardNumberMasked(String cardNumberMasked) {
            target.cardNumberMasked = cardNumberMasked;
            return this;
        }

        public Builder merchantName(String merchantName) {
            target.merchantName = merchantName;
            return this;
        }

        public Builder merchantLocation(String merchantLocation) {
            target.merchantLocation = merchantLocation;
            return this;
        }

        public Builder gallons(BigDecimal gallons) {
            target.gallons = gallons;
            return this;
        }

        public Builder pricePerGallon(BigDecimal pricePerGallon) {
            target.pricePerGallon = pricePerGallon;
            return this;
        }

        public Builder totalAmount(BigDecimal totalAmount) {
            target.totalAmount = totalAmount;
            return this;
        }

        public Builder transactionTimestamp(Instant transactionTimestamp) {
            target.transactionTimestamp = transactionTimestamp;
            return this;
        }

        public FuelTransactionWebhookRequest build() {
            return target;
        }
    }
}
