package com.frederico.coupon.interfaces.rest.coupon;

import com.frederico.coupon.application.coupon.CreateCouponCommand;
import com.frederico.coupon.application.coupon.CreateCouponUseCase;
import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

	@Mock
	private CreateCouponUseCase createCouponUseCase;

	@InjectMocks
	private CouponController controller;

	@Test
	void shouldCreateCoupon() {
		var coupon = Coupon.create(
				"ABC-123",
				"10% discount",
				new BigDecimal("10.00"),
				Instant.now().plusSeconds(3600),
				true
		);

		when(createCouponUseCase.execute(any()))
				.thenReturn(coupon);

		var request = new CreateCouponRequest(
				"ABC-123",
				"10% discount",
				new BigDecimal("10.00"),
				coupon.getExpirationDate(),
				true
		);

		var response = controller.create(request);

		assertThat(response.id()).isEqualTo(coupon.getId());
		assertThat(response.code()).isEqualTo("ABC123");
		assertThat(response.description()).isEqualTo("10% discount");
		assertThat(response.discountValue()).isEqualByComparingTo("10.00");
		assertThat(response.status()).isEqualTo(CouponStatus.ACTIVE);
		assertThat(response.published()).isTrue();
		assertThat(response.redeemed()).isFalse();

		verify(createCouponUseCase).execute(any(CreateCouponCommand.class));
	}

	@Test
	void shouldUseFalseWhenPublishedIsNull() {
		var coupon = Coupon.create(
				"ABC123",
				"10% discount",
				new BigDecimal("10.00"),
				Instant.now().plusSeconds(3600),
				false
		);

		when(createCouponUseCase.execute(any()))
				.thenReturn(coupon);

		var request = new CreateCouponRequest(
				"ABC123",
				"10% discount",
				new BigDecimal("10.00"),
				coupon.getExpirationDate(),
				null
		);

		var response = controller.create(request);

		assertThat(response.published()).isFalse();
		assertThat(response.status()).isEqualTo(CouponStatus.INACTIVE);

		var captor = ArgumentCaptor.forClass(CreateCouponCommand.class);

		verify(createCouponUseCase).execute(captor.capture());

		assertThat(captor.getValue().published()).isFalse();
	}
}