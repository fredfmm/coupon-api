package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponRepository;
import com.frederico.coupon.domain.coupon.CouponStatus;
import com.frederico.coupon.domain.coupon.exception.CouponAlreadyDeletedException;
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

class DeleteCouponServiceTest {

    private CouponRepository couponRepository;
    private DeleteCouponService service;

    @BeforeEach
    void setUp() {
        couponRepository = mock(CouponRepository.class);
        service = new DeleteCouponService(couponRepository);
    }

    @Test
    void shouldDeleteCoupon() {

        var id = UUID.randomUUID();

        var coupon = Coupon.restore(
                id,
                "ABC123",
                "Test coupon",
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                CouponStatus.ACTIVE,
                true,
                false
        );

        when(couponRepository.findById(id))
                .thenReturn(Optional.of(coupon));

        service.execute(id);

        assertThat(coupon.getStatus())
                .isEqualTo(CouponStatus.DELETED);

        verify(couponRepository).findById(id);
        verify(couponRepository).save(coupon);
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
        verify(couponRepository, never()).save(any());
    }

    @Test
    void shouldNotDeleteCouponThatIsAlreadyDeleted() {

        var id = UUID.randomUUID();

        var coupon = Coupon.restore(
                id,
                "ABC123",
                "Deleted coupon",
                new BigDecimal("10.00"),
                Instant.now().plusSeconds(3600),
                CouponStatus.DELETED,
                true,
                false
        );

        when(couponRepository.findById(id))
                .thenReturn(Optional.of(coupon));

        assertThatThrownBy(() -> service.execute(id))
                .isInstanceOf(CouponAlreadyDeletedException.class)
                .hasMessage("Coupon has already been deleted");

        assertThat(coupon.getStatus())
                .isEqualTo(CouponStatus.DELETED);

        verify(couponRepository).findById(id);
        verify(couponRepository, never()).save(any());
    }
}