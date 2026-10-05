package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.CouponRepository;
import com.frederico.coupon.domain.coupon.exception.CouponNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCouponService implements DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    public DeleteCouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public void execute(UUID id) {
        var coupon = couponRepository.findById(id)
                .orElseThrow(() ->
                        new CouponNotFoundException(
                                "Coupon not found: " + id
                        )
                );

        coupon.delete();

        couponRepository.save(coupon);
    }
}