package com.portfolio.fueltx.model.enums;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.enums
 * @since 2026-09-10T12:20:00
 */
public enum SummaryPeriod {
    WEEK(ChronoUnit.WEEKS, 1),
    MONTH(ChronoUnit.MONTHS, 1);

    private final ChronoUnit unit;
    private final long amount;

    SummaryPeriod(ChronoUnit unit, long amount) {
        this.unit = unit;
        this.amount = amount;
    }

    public Instant periodStart(Instant end) {
        return end.atZone(ZoneOffset.UTC).minus(amount, unit).toInstant();
    }

    public static SummaryPeriod fromQueryParam(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("period is required (week|month)");
        }
        try {
            return SummaryPeriod.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported period '" + value + "'. Use week|month");
        }
    }
}
