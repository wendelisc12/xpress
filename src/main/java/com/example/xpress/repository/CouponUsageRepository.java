package com.example.xpress.repository;

import com.example.xpress.entities.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    List<CouponUsage> findByUserId(Long userId);
    List<CouponUsage> findByCouponId(Long couponId);
}
