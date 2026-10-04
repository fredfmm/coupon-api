package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.Coupon;

import java.util.UUID;

public interface GetCouponUseCase {

    Coupon execute(UUID id);
}