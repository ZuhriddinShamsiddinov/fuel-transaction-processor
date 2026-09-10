package com.portfolio.fueltx.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
public class DriverFuelSummary {

    private String driverId;
    private BigDecimal totalGallons;
    private BigDecimal totalSpend;
    private int transactionCount;
    private Instant periodStart;
    private Instant periodEnd;
    private String period;

    public DriverFuelSummary() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public BigDecimal getTotalGallons() {
        return totalGallons;
    }

    public void setTotalGallons(BigDecimal totalGallons) {
        this.totalGallons = totalGallons;
    }

    public BigDecimal getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(BigDecimal totalSpend) {
        this.totalSpend = totalSpend;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Instant getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Instant periodStart) {
        this.periodStart = periodStart;
    }

    public Instant getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Instant periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public static final class Builder {
        private final DriverFuelSummary target = new DriverFuelSummary();

        public Builder driverId(String driverId) {
            target.driverId = driverId;
            return this;
        }

        public Builder totalGallons(BigDecimal totalGallons) {
            target.totalGallons = totalGallons;
            return this;
        }

        public Builder totalSpend(BigDecimal totalSpend) {
            target.totalSpend = totalSpend;
            return this;
        }

        public Builder transactionCount(int transactionCount) {
            target.transactionCount = transactionCount;
            return this;
        }

        public Builder periodStart(Instant periodStart) {
            target.periodStart = periodStart;
            return this;
        }

        public Builder periodEnd(Instant periodEnd) {
            target.periodEnd = periodEnd;
            return this;
        }

        public Builder period(String period) {
            target.period = period;
            return this;
        }

        public DriverFuelSummary build() {
            return target;
        }
    }
}
