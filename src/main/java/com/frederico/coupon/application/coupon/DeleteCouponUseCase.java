package com.frederico.coupon.application.coupon;

import java.util.UUID;

public interface DeleteCouponUseCase {

    void execute(UUID id);
}