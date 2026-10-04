package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponRepository;
import com.frederico.coupon.domain.coupon.CouponStatus;
import com.frederico.coupon.domain.coupon.exception.CouponNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetCouponServiceTest {

    private CouponRepository couponRepository;
    private GetCouponService service;

    @BeforeEach
    void setUp() {
        couponRepository = mock(CouponRepository.class);
        service = new GetCouponService(couponRepository);
    }

    @Test
    void shouldReturnCouponWhenExists() {

        var id = UUID.randomUUID();
        var expirationDate = Instant.now().plusSeconds(3600);

        var coupon = Coupon.restore(
                id,
                "ABC123",
                "Test coupon",
                new BigDecimal("10.00"),
                expirationDate,
                CouponStatus.ACTIVE,
                true,
                false
        );

        when(couponRepository.findById(id))
                .thenReturn(Optional.of(coupon));

        var result = service.execute(id);

        assertThat(result).isSameAs(coupon);

        verify(couponRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenCouponDoesNotExist() {

        var id = UUID.randomUUID();

        when(couponRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.execute(id))
                .isInstanceOf(CouponNotFoundException.class)
                .hasMessage("Coupon not found: " + id);

        verify(couponRepository).findById(id);
    }
}