package com.frederico.coupon.domain.coupon.exception;

public class CouponNotFoundException extends InvalidCouponException {

    public CouponNotFoundException(String message) {
        super(message);
    }
}