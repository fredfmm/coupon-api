package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.Coupon;

public interface CreateCouponUseCase {

    Coupon execute(CreateCouponCommand command);
}