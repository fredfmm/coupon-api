package com.frederico.coupon.infrastructure.persistence.coupon;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, UUID> {
}