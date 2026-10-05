package com.frederico.coupon.interfaces.rest;

import com.frederico.coupon.application.coupon.CreateCouponCommand;
import com.frederico.coupon.application.coupon.CreateCouponUseCase;
import com.frederico.coupon.application.coupon.DeleteCouponUseCase;
import com.frederico.coupon.application.coupon.GetCouponUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final GetCouponUseCase getCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;

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

    @GetMapping("/{id}")
    public CouponResponse getById(@PathVariable UUID id) {
        var coupon = getCouponUseCase.execute(id);

        return CouponResponse.fromDomain(coupon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteCouponUseCase.execute(id);
    }
}