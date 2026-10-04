package com.frederico.coupon.application.coupon;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponCommand(
        String code,
        String description,
        BigDecimal discountValue,
        Instant expirationDate,
        boolean published
) {
}