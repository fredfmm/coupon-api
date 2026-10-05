package com.frederico.coupon.interfaces.rest;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CouponResponse(

        @Schema(
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID id,

        @Schema(
                example = "ABC123",
                description = "Normalized coupon code"
        )
        String code,

        @Schema(
                example = "10% discount coupon"
        )
        String description,

        @Schema(
                example = "10.00"
        )
        BigDecimal discountValue,

        @Schema(
                example = "2027-12-31T23:59:59Z"
        )
        Instant expirationDate,

        @Schema(
                example = "ACTIVE",
                description = "Coupon status"
        )
        CouponStatus status,

        @Schema(
                example = "true"
        )
        boolean published,

        @Schema(
                example = "false"
        )
        boolean redeemed
) {

    public static CouponResponse fromDomain(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscountValue(),
                coupon.getExpirationDate(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed()
        );
    }
}