package com.portfolio.fueltx.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zuhriddin
 * @see com.portfolio.fueltx.model.dto
 * @since 2026-09-10T12:15:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReviewRequest {

    @NotNull
    private ReviewDecision decision;

    public enum ReviewDecision {
        APPROVED,
        REJECTED
    }
}
