package com.frederico.coupon.interfaces.rest;

import com.frederico.coupon.application.coupon.CreateCouponUseCase;
import com.frederico.coupon.application.coupon.GetCouponUseCase;
import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CouponControllerTest {

	private final CreateCouponUseCase createCouponUseCase =
			mock(CreateCouponUseCase.class);

	private final GetCouponUseCase getCouponUseCase =
			mock(GetCouponUseCase.class);

	private final MockMvc mockMvc = MockMvcBuilders
			.standaloneSetup(
					new CouponController(
							createCouponUseCase,
							getCouponUseCase
					)
			)
			.build();

	@Test
	void shouldGetCouponById() throws Exception {

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

		when(getCouponUseCase.execute(id))
				.thenReturn(coupon);

		mockMvc.perform(
						get("/coupon/{id}", id)
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.code").value("ABC123"))
				.andExpect(jsonPath("$.description").value("Test coupon"))
				.andExpect(jsonPath("$.discountValue").value(10.00))
				.andExpect(jsonPath("$.status").value("ACTIVE"))
				.andExpect(jsonPath("$.published").value(true))
				.andExpect(jsonPath("$.redeemed").value(false));

		verify(getCouponUseCase).execute(id);
	}
}