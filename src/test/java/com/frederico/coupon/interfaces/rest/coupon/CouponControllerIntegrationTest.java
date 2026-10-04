package com.frederico.coupon.interfaces.rest.coupon;

import com.frederico.coupon.domain.coupon.CouponStatus;
import com.frederico.coupon.infrastructure.persistence.coupon.CouponJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.MICROS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CouponJpaRepository repository;

	@AfterEach
	void cleanDatabase() {
		repository.deleteAll();
	}

	@Test
	void shouldCreatePublishedCoupon() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "AB-C123",
                    "description": "Discount coupon",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.code").value("ABC123"))
				.andExpect(jsonPath("$.description").value("Discount coupon"))
				.andExpect(jsonPath("$.discountValue").value(10.00))
				.andExpect(jsonPath("$.status")
						.value(CouponStatus.ACTIVE.name()))
				.andExpect(jsonPath("$.published").value(true))
				.andExpect(jsonPath("$.redeemed").value(false));

		var coupons = repository.findAll();

		assertThat(coupons).hasSize(1);
		assertThat(coupons.getFirst().getCode()).isEqualTo("ABC123");
		assertThat(coupons.getFirst().isPublished()).isTrue();
	}

	@Test
	void shouldCreateInactiveCouponWhenPublishedIsFalse() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "ABC123",
                    "description": "Inactive coupon",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": false
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.code").value("ABC123"))
				.andExpect(jsonPath("$.status")
						.value(CouponStatus.INACTIVE.name()))
				.andExpect(jsonPath("$.published").value(false))
				.andExpect(jsonPath("$.redeemed").value(false));
	}

	@Test
	void shouldCreateInactiveCouponWhenPublishedIsNotProvided() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "ABC123",
                    "description": "Coupon without published",
                    "discountValue": 10.00,
                    "expirationDate": "%s"
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.code").value("ABC123"))
				.andExpect(jsonPath("$.status")
						.value(CouponStatus.INACTIVE.name()))
				.andExpect(jsonPath("$.published").value(false))
				.andExpect(jsonPath("$.redeemed").value(false));
	}

	@Test
	void shouldRemoveSpecialCharactersFromCode() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "A-B.C/123",
                    "description": "Coupon with special characters",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value("ABC123"));
	}

	@Test
	void shouldRejectRequestWhenCodeIsInvalid() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "ABC12",
                    "description": "Invalid code",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Coupon code must contain exactly 6 alphanumeric characters"));
	}

	@Test
	void shouldRejectRequestWhenCodeHasMoreThanSixCharacters() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "ABCDEFG",
                    "description": "Invalid code",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Coupon code must contain exactly 6 alphanumeric characters"));
	}

	@Test
	void shouldRejectRequestWhenDiscountIsLessThanMinimum() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "ABC123",
                    "description": "Invalid discount",
                    "discountValue": 0.49,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Discount value must be at least 0.5"));
	}

	@Test
	void shouldAcceptMinimumDiscount() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "ABC123",
                    "description": "Minimum discount",
                    "discountValue": 0.50,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.discountValue").value(0.50));
	}

	@Test
	void shouldRejectRequestWhenExpirationDateIsInThePast() throws Exception {

		var expirationDate = Instant.now().minusSeconds(3600);

		var request = """
                {
                    "code": "ABC123",
                    "description": "Expired coupon",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Expiration date cannot be in the past"));
	}

	@Test
	void shouldRejectRequestWhenDescriptionIsBlank() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "ABC123",
                    "description": "",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldRejectRequestWhenRequiredFieldsAreMissing() throws Exception {

		var request = """
                {
                    "code": "",
                    "description": "",
                    "discountValue": null,
                    "expirationDate": null
                }
                """;

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldRejectRequestWhenCodeIsNull() throws Exception {

		var request = """
                {
                    "code": null,
                    "description": "Valid description",
                    "discountValue": 10.00,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(Instant.now().plusSeconds(3600));

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldRejectRequestWhenDiscountIsNull() throws Exception {

		var request = """
                {
                    "code": "ABC123",
                    "description": "Valid description",
                    "discountValue": null,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(Instant.now().plusSeconds(3600));

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldRejectRequestWhenExpirationDateIsNull() throws Exception {

		var request = """
                {
                    "code": "ABC123",
                    "description": "Valid description",
                    "discountValue": 10.00,
                    "expirationDate": null,
                    "published": true
                }
                """;

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldPersistAllCouponData() throws Exception {

		var expirationDate = Instant.now().plusSeconds(3600);

		var request = """
                {
                    "code": "A-B1234",
                    "description": "Persistent coupon",
                    "discountValue": 25.50,
                    "expirationDate": "%s",
                    "published": true
                }
                """.formatted(expirationDate);

		mockMvc.perform(
						post("/coupon")
								.contentType(MediaType.APPLICATION_JSON)
								.content(request)
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value("AB1234"));

		var coupons = repository.findAll();

		assertThat(coupons).hasSize(1);

		var coupon = coupons.getFirst();

		assertThat(coupon.getCode()).isEqualTo("AB1234");
		assertThat(coupon.getDescription()).isEqualTo("Persistent coupon");
		assertThat(coupon.getDiscountValue())
				.isEqualByComparingTo("25.50");
		assertThat(coupon.getExpirationDate())
				.isCloseTo(expirationDate, within(1, MICROS));
		assertThat(coupon.getStatus())
				.isEqualTo(CouponStatus.ACTIVE);
		assertThat(coupon.isPublished()).isTrue();
		assertThat(coupon.isRedeemed()).isFalse();
	}
}