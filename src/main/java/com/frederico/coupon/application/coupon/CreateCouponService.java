package com.frederico.coupon.application.coupon;


import com.frederico.coupon.domain.coupon.Coupon;

public interface CreateCouponService {

    Coupon execute(CreateCouponCommand command);
}