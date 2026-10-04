package com.frederico.coupon.application.coupon;


import com.frederico.coupon.domain.coupon.Coupon;

public interface CreateCouponCommand {

    Coupon execute(CreateCouponCommand command);
}