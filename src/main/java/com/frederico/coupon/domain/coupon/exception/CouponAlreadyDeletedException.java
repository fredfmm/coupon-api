package com.frederico.coupon.domain.coupon.exception;

public class CouponAlreadyDeletedException extends InvalidCouponException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}