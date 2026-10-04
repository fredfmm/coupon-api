package com.frederico.coupon.interfaces.rest;

import com.frederico.coupon.domain.coupon.exception.CouponNotFoundException;
import com.frederico.coupon.domain.coupon.exception.InvalidCouponException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCouponException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidCouponException(
            InvalidCouponException exception
    ) {
        return Map.of(
                "message", exception.getMessage()
        );
    }

    @ExceptionHandler(CouponNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleCouponNotFoundException(
            CouponNotFoundException exception
    ) {
        return Map.of("message", exception.getMessage());
    }
}