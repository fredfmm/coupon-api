package com.frederico.coupon.interfaces.rest;

import com.frederico.coupon.application.coupon.CreateCouponCommand;
import com.frederico.coupon.application.coupon.CreateCouponUseCase;
import com.frederico.coupon.application.coupon.DeleteCouponUseCase;
import com.frederico.coupon.application.coupon.GetCouponUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Tag(name = "Coupons", description = "Coupon management API")
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final GetCouponUseCase getCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;

    @Operation(summary = "Create a coupon")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Coupon created"),
            @ApiResponse(responseCode = "400", description = "Invalid coupon data")
    })
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

    @Operation(summary = "Get a coupon by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coupon found"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @GetMapping("/{id}")
    public CouponResponse getById(@PathVariable UUID id) {
        var coupon = getCouponUseCase.execute(id);

        return CouponResponse.fromDomain(coupon);
    }

    @Operation(summary = "Soft delete a coupon")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Coupon deleted"),
            @ApiResponse(responseCode = "400", description = "Coupon already deleted"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteCouponUseCase.execute(id);
    }
}