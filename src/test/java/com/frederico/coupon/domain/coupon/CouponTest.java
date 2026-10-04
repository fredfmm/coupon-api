package com.frederico.coupon.domain.coupon;

import com.frederico.coupon.domain.coupon.exception.CouponAlreadyDeletedException;
import com.frederico.coupon.domain.coupon.exception.InvalidCouponCodeException;
import com.frederico.coupon.domain.coupon.exception.InvalidCouponException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

	@Test
	void shouldCreateActiveCouponWhenPublished() {
		var coupon = createValidCoupon(true);

		assertThat(coupon.getStatus())
				.isEqualTo(CouponStatus.ACTIVE);

		assertThat(coupon.isPublished())
				.isTrue();

		assertThat(coupon.isRedeemed())
				.isFalse();
	}

	@Test
	void shouldCreateInactiveCouponWhenNotPublished() {
		var coupon = createValidCoupon(false);

		assertThat(coupon.getStatus())
				.isEqualTo(CouponStatus.INACTIVE);

		assertThat(coupon.isPublished())
				.isFalse();
	}

	@Test
	void shouldRemoveSpecialCharactersFromCode() {
		var coupon = Coupon.create(
				"ABC-123",
				"Test coupon",
				new BigDecimal("0.5"),
				Instant.now().plusSeconds(3600),
				false
		);

		assertThat(coupon.getCode())
				.isEqualTo("ABC123");
	}

	@Test
	void shouldRejectCodeWhenResultHasLessThanSixCharacters() {
		assertThatThrownBy(() ->
				Coupon.create(
						"ABC-12",
						"Test coupon",
						new BigDecimal("0.5"),
						Instant.now().plusSeconds(3600),
						false
				)
		)
				.isInstanceOf(InvalidCouponCodeException.class);
	}

	@Test
	void shouldRejectCodeWhenResultHasMoreThanSixCharacters() {
		assertThatThrownBy(() ->
				Coupon.create(
						"ABCDEFG",
						"Test coupon",
						new BigDecimal("0.5"),
						Instant.now().plusSeconds(3600),
						false
				)
		)
				.isInstanceOf(InvalidCouponCodeException.class);
	}

	@Test
	void shouldRejectDiscountBelowMinimum() {
		assertThatThrownBy(() ->
				Coupon.create(
						"ABC123",
						"Test coupon",
						new BigDecimal("0.49"),
						Instant.now().plusSeconds(3600),
						false
				)
		)
				.isInstanceOf(InvalidCouponException.class);
	}

	@Test
	void shouldAcceptMinimumDiscount() {
		var coupon = Coupon.create(
				"ABC123",
				"Test coupon",
				new BigDecimal("0.5"),
				Instant.now().plusSeconds(3600),
				false
		);

		assertThat(coupon.getDiscountValue())
				.isEqualByComparingTo("0.5");
	}

	@Test
	void shouldRejectExpirationDateInThePast() {
		assertThatThrownBy(() ->
				Coupon.create(
						"ABC123",
						"Test coupon",
						new BigDecimal("0.5"),
						Instant.now().minusSeconds(1),
						false
				)
		)
				.isInstanceOf(InvalidCouponException.class);
	}

	@Test
	void shouldRejectBlankDescription() {
		assertThatThrownBy(() ->
				Coupon.create(
						"ABC123",
						" ",
						new BigDecimal("0.5"),
						Instant.now().plusSeconds(3600),
						false
				)
		)
				.isInstanceOf(InvalidCouponException.class);
	}

	@Test
	void shouldDeleteCoupon() {
		var coupon = createValidCoupon(true);

		coupon.delete();

		assertThat(coupon.getStatus())
				.isEqualTo(CouponStatus.DELETED);
	}

	@Test
	void shouldNotDeleteCouponTwice() {
		var coupon = createValidCoupon(true);

		coupon.delete();

		assertThatThrownBy(coupon::delete)
				.isInstanceOf(CouponAlreadyDeletedException.class);
	}

	private Coupon createValidCoupon(boolean published) {
		return Coupon.create(
				"ABC123",
				"Test coupon",
				new BigDecimal("10.00"),
				Instant.now().plusSeconds(3600),
				published
		);
	}
}