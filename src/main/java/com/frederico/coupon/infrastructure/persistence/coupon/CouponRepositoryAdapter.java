package com.frederico.coupon.infrastructure.persistence.coupon;

import com.frederico.coupon.domain.coupon.Coupon;
import com.frederico.coupon.domain.coupon.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CouponRepositoryAdapter implements CouponRepository {

    private final CouponJpaRepository repository;

    public CouponRepositoryAdapter(CouponJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        var entity = CouponEntity.fromDomain(coupon);
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return repository.findById(id)
                .map(CouponEntity::toDomain);
    }
}