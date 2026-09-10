package com.portfolio.fueltx.model.entity;

import com.portfolio.fueltx.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Builder.Default
    private boolean newEntity = true;

    @Override
    public boolean isNew() {
        return newEntity;
    }

    public void markPersisted() {
        this.newEntity = false;
    }
}
