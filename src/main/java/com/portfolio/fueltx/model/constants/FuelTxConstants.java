package com.portfolio.fueltx.model.constants;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.constants
 * @since 2026-09-10T12:20:00
 */
public final class FuelTxConstants {

    public static final String WEBHOOK_SIGNATURE_HEADER = "X-Webhook-Signature";
    public static final String HMAC_ALGORITHM = "HmacSHA256";

    public static final BigDecimal MAX_GALLONS_PER_FILL = new BigDecimal("200");
    public static final Duration RAPID_FILL_WINDOW = Duration.ofMinutes(5);
    public static final long RAPID_FILL_THRESHOLD = 2;

    public static final String CACHE_KEY_PREFIX = "driver:fuel-summary:";
    public static final String WS_TRANSACTIONS_PATH = "/ws/transactions";

    private FuelTxConstants() {
    }
}
