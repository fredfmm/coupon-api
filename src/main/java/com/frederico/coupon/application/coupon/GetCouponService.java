package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponRepository;
import com.frederico.coupon.domain.coupon.exception.CouponNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCouponService implements GetCouponUseCase {

    private final CouponRepository couponRepository;

    public GetCouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon execute(UUID id) {
        return couponRepository.findById(id)
                .orElseThrow(() ->
                        new CouponNotFoundException(
                                "Coupon not found: " + id
                        )
                );
    }
}