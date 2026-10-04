package com.frederico.coupon.domain.coupon.exception;

public class InvalidCouponException extends RuntimeException {

    public InvalidCouponException(String message) {
        super(message);
    }
}