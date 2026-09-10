package com.portfolio.fueltx.service.impl;

import com.portfolio.fueltx.config.FuelTxProperties;
import com.portfolio.fueltx.exception.InvalidWebhookSignatureException;
import com.portfolio.fueltx.model.constants.FuelTxConstants;
import com.portfolio.fueltx.service.WebhookValidationService;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.service.impl
 * @since 2026-09-10T12:55:00
 */
@Service
public class WebhookValidationServiceImpl implements WebhookValidationService {

    private final FuelTxProperties properties;

    public WebhookValidationServiceImpl(FuelTxProperties properties) {
        this.properties = properties;
    }

    @Override
    public void validateSignature(String rawBody, String providedSignature) {
        if (providedSignature == null || providedSignature.isBlank()) {
            throw new InvalidWebhookSignatureException("Missing webhook signature");
        }

        String expected = computeHmacHex(rawBody == null ? "" : rawBody);
        String normalized = normalizeSignature(providedSignature);

        if (!MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                normalized.getBytes(StandardCharsets.UTF_8))) {
            throw new InvalidWebhookSignatureException("Invalid webhook signature");
        }
    }

    @Override
    public String computeHmacHex(String payload) {
        try {
            Mac mac = Mac.getInstance(FuelTxConstants.HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(
                    properties.getWebhook().getSecret().getBytes(StandardCharsets.UTF_8),
                    FuelTxConstants.HMAC_ALGORITHM));
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to compute webhook signature", ex);
        }
    }

    private String normalizeSignature(String signature) {
        String trimmed = signature.trim();
        if (trimmed.regionMatches(true, 0, "sha256=", 0, 7)) {
            return trimmed.substring(7).trim().toLowerCase();
        }
        return trimmed.toLowerCase();
    }
}
