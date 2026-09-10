package com.portfolio.fueltx.model.dto;

import jakarta.validation.constraints.NotNull;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
public class TransactionReviewRequest {

    @NotNull
    private ReviewDecision decision;

    public TransactionReviewRequest() {
    }

    public TransactionReviewRequest(ReviewDecision decision) {
        this.decision = decision;
    }

    public ReviewDecision getDecision() {
        return decision;
    }

    public void setDecision(ReviewDecision decision) {
        this.decision = decision;
    }

    public enum ReviewDecision {
        APPROVED,
        REJECTED
    }
}
