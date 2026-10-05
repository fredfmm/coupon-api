package com.frederico.coupon.interfaces.rest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponRequest(
        @Schema(example = "ABC-123")
        @NotBlank
        String code,

        @Schema(example = "10% discount coupon")
        @NotBlank
        String description,

        @Schema(example = "10.00")
        @NotNull
        BigDecimal discountValue,

        @Schema(example = "2027-12-31T23:59:59Z")
        @NotNull
        Instant expirationDate,

        @Schema(example = "true")
        Boolean published
) {
}