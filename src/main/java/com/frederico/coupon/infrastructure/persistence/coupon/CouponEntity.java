package com.frederico.coupon.infrastructure.persistence.coupon;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CouponEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private Instant expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    public static CouponEntity fromDomain(Coupon coupon) {
        return CouponEntity.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description(coupon.getDescription())
                .discountValue(coupon.getDiscountValue())
                .expirationDate(coupon.getExpirationDate())
                .status(coupon.getStatus())
                .published(coupon.isPublished())
                .redeemed(coupon.isRedeemed())
                .build();
    }

    public Coupon toDomain() {
        return Coupon.restore(
                id,
                code,
                description,
                discountValue,
                expirationDate,
                status,
                published,
                redeemed
        );
    }
}