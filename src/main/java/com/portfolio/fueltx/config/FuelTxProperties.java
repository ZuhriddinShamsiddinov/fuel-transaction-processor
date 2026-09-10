package com.portfolio.fueltx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Application-specific configuration properties.
 *
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:15:00
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "fueltx")
public class FuelTxProperties {

    private final Webhook webhook = new Webhook();
    private final Jms jms = new Jms();
    private final Cache cache = new Cache();

    @Getter
    @Setter
    public static class Webhook {
        private String secret;
    }

    @Getter
    @Setter
    public static class Jms {
        private String queue = "fuel-transaction-events";
    }

    @Getter
    @Setter
    public static class Cache {
        private Duration summaryTtl = Duration.ofHours(1);
    }
}
