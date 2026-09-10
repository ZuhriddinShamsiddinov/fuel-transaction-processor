package com.portfolio.fueltx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.config
 * @since 2026-09-10T12:20:00
 */
@ConfigurationProperties(prefix = "fueltx")
public class FuelTxProperties {

    private final Webhook webhook = new Webhook();
    private final Jms jms = new Jms();
    private final Cache cache = new Cache();

    public Webhook getWebhook() {
        return webhook;
    }

    public Jms getJms() {
        return jms;
    }

    public Cache getCache() {
        return cache;
    }

    public static class Webhook {
        private String secret;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }

    public static class Jms {
        private String queue = "fuel-transaction-events";

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }
    }

    public static class Cache {
        private Duration summaryTtl = Duration.ofHours(1);

        public Duration getSummaryTtl() {
            return summaryTtl;
        }

        public void setSummaryTtl(Duration summaryTtl) {
            this.summaryTtl = summaryTtl;
        }
    }
}
