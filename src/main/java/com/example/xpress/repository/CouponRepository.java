package com.example.xpress.repository;

import com.example.xpress.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCoupon(String coupon);
}
