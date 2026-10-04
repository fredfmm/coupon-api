package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCouponService implements CreateCouponUseCase {

    private final CouponRepository couponRepository;

    public CreateCouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon execute(CreateCouponCommand command) {
        var coupon = Coupon.create(
                command.code(),
                command.description(),
                command.discountValue(),
                command.expirationDate(),
                command.published()
        );

        return couponRepository.save(coupon);
    }
}