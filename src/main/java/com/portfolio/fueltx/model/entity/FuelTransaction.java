package com.portfolio.fueltx.model.entity;

import com.portfolio.fueltx.model.enums.TransactionStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.entity
 * @since 2026-09-10T12:20:00
 */
@Table("fuel_transactions")
public class FuelTransaction implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("external_transaction_id")
    private String externalTransactionId;

    @Column("driver_id")
    private String driverId;

    @Column("vehicle_id")
    private String vehicleId;

    @Column("card_number_masked")
    private String cardNumberMasked;

    @Column("merchant_name")
    private String merchantName;

    @Column("merchant_location")
    private String merchantLocation;

    private BigDecimal gallons;

    @Column("price_per_gallon")
    private BigDecimal pricePerGallon;

    @Column("total_amount")
    private BigDecimal totalAmount;

    @Column("transaction_timestamp")
    private Instant transactionTimestamp;

    private TransactionStatus status;

    @Column("created_at")
    private Instant createdAt;

    @Column("processed_at")
    private Instant processedAt;

    @Transient
    private boolean newEntity = true;

    public FuelTransaction() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public boolean isNew() {
        return newEntity;
    }

    public void markPersisted() {
        this.newEntity = false;
    }

    public static final class Builder {
        private final FuelTransaction target = new FuelTransaction();

        public Builder id(UUID id) {
            target.id = id;
            return this;
        }

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

        public Builder status(TransactionStatus status) {
            target.status = status;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            target.createdAt = createdAt;
            return this;
        }

        public Builder processedAt(Instant processedAt) {
            target.processedAt = processedAt;
            return this;
        }

        public FuelTransaction build() {
            return target;
        }
    }
}
