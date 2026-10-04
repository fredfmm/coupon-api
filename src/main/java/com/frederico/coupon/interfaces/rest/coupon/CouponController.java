package com.frederico.coupon.interfaces.rest.coupon;

import com.frederico.coupon.application.coupon.CreateCouponCommand;
import com.frederico.coupon.application.coupon.CreateCouponUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase) {
        this.createCouponUseCase = createCouponUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(
            @Valid @RequestBody CreateCouponRequest request
    ) {
        var command = new CreateCouponCommand(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                Boolean.TRUE.equals(request.published())
        );

        var coupon = createCouponUseCase.execute(command);

        return CouponResponse.fromDomain(coupon);
    }
}