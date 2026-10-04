package com.frederico.coupon.domain.coupon;

import com.frederico.coupon.domain.coupon.exception.CouponAlreadyDeletedException;
import com.frederico.coupon.domain.coupon.exception.InvalidCouponCodeException;
import com.frederico.coupon.domain.coupon.exception.InvalidCouponException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Coupon {

    private static final int CODE_LENGTH = 6;
    private static final BigDecimal MINIMUM_DISCOUNT = new BigDecimal("0.5");

    private final UUID id;
    private final String code;
    private final String description;
    private final BigDecimal discountValue;
    private final Instant expirationDate;

    private CouponStatus status;
    private boolean published;
    private boolean redeemed;

    private Coupon(
            UUID id,
            String code,
            String description,
            BigDecimal discountValue,
            Instant expirationDate,
            CouponStatus status,
            boolean published,
            boolean redeemed
    ) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }

    public static Coupon create(
            String code,
            String description,
            BigDecimal discountValue,
            Instant expirationDate,
            boolean published
    ) {
        var normalizedCode = normalizeCode(code);

        validateDescription(description);
        validateDiscount(discountValue);
        validateExpirationDate(expirationDate);

        return new Coupon(
                UUID.randomUUID(),
                normalizedCode,
                description,
                discountValue,
                expirationDate,
                published ? CouponStatus.ACTIVE : CouponStatus.INACTIVE,
                published,
                false
        );
    }

    public static Coupon restore(
            UUID id,
            String code,
            String description,
            BigDecimal discountValue,
            Instant expirationDate,
            CouponStatus status,
            boolean published,
            boolean redeemed
    ) {
        return new Coupon(
                id,
                code,
                description,
                discountValue,
                expirationDate,
                status,
                published,
                redeemed
        );
    }

    public void delete() {
        if (this.status == CouponStatus.DELETED) {
            throw new CouponAlreadyDeletedException(
                    "Coupon has already been deleted"
            );
        }

        this.status = CouponStatus.DELETED;
    }

    private static String normalizeCode(String code) {
        if (code == null) {
            throw new InvalidCouponCodeException(
                    "Coupon code is required"
            );
        }

        var normalizedCode = code.replaceAll("[^a-zA-Z0-9]", "");

        if (normalizedCode.length() != CODE_LENGTH) {
            throw new InvalidCouponCodeException(
                    "Coupon code must contain exactly 6 alphanumeric characters"
            );
        }

        return normalizedCode;
    }

    private static void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidCouponException(
                    "Coupon description is required"
            );
        }
    }

    private static void validateDiscount(BigDecimal discountValue) {
        if (discountValue == null) {
            throw new InvalidCouponException(
                    "Discount value is required"
            );
        }

        if (discountValue.compareTo(MINIMUM_DISCOUNT) < 0) {
            throw new InvalidCouponException(
                    "Discount value must be at least 0.5"
            );
        }
    }

    private static void validateExpirationDate(Instant expirationDate) {
        if (expirationDate == null) {
            throw new InvalidCouponException(
                    "Expiration date is required"
            );
        }

        if (expirationDate.isBefore(Instant.now())) {
            throw new InvalidCouponException(
                    "Expiration date cannot be in the past"
            );
        }
    }
}