package com.example.xpress.repository;

import com.example.xpress.entities.UsedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedCouponRepository  extends JpaRepository<UsedCoupon, Long> {
}
