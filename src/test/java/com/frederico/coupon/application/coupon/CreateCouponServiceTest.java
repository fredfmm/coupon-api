package com.frederico.coupon.application.coupon;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCouponServiceTest {

	@Mock
	private CouponRepository couponRepository;

	private CreateCouponService createCouponService;

	@BeforeEach
	void setUp() {
		createCouponService = new CreateCouponService(couponRepository);
	}

	@Test
	void shouldCreateAndSaveCoupon() {
		var command = new CreateCouponCommand(
				"ABC-123",
				"Test coupon",
				new BigDecimal("10.00"),
				Instant.now().plusSeconds(3600),
				true
		);

		when(couponRepository.save(any(Coupon.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		var result = createCouponService.execute(command);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isNotNull();
		assertThat(result.getCode()).isEqualTo("ABC123");
		assertThat(result.getDescription()).isEqualTo("Test coupon");
		assertThat(result.getDiscountValue())
				.isEqualByComparingTo("10.00");
		assertThat(result.isPublished()).isTrue();

		verify(couponRepository).save(any(Coupon.class));
	}
}