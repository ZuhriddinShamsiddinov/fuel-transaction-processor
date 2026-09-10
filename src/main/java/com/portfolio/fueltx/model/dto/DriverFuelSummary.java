package com.portfolio.fueltx.model.dto;

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
public class DriverFuelSummary {

    private String driverId;
    private BigDecimal totalGallons;
    private BigDecimal totalSpend;
    private int transactionCount;
    private Instant periodStart;
    private Instant periodEnd;
    private String period;
}
