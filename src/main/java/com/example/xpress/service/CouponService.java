package com.example.xpress.service;

import com.example.xpress.entities.*;
import com.example.xpress.repository.CouponRepository;
import com.example.xpress.repository.CouponUsageRepository;
import com.example.xpress.repository.UsedCouponRepository;
import com.example.xpress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponUsageRepository couponUsageRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    UsedCouponRepository usedCouponRepository;

    public Coupon createCoupon(Coupon coupon){
        if(coupon.getExpirationDate() == null){
            coupon.setExpirationDate(LocalDate.now().plusYears(10));
        }

        if(couponRepository.findByCoupon(coupon.getCoupon()) != null){
            throw new RuntimeException("This coupon already exists.");
        }

        if(coupon.getUsageLimit() <= 0){
            throw new RuntimeException("The usage limit cannot be less than 1.");
        }

        if(coupon.getExpirationDate().isBefore(LocalDate.now())){
            throw new RuntimeException("The expiration date cannot be expired.");
        }

        coupon.setUsedCount(0);
        coupon.setCreatedAt(LocalDate.now());

        return couponRepository.save(coupon);
    }

    public double useCoupon(String token, String coupon, double price){
        Coupon couponUse = couponRepository.findByCoupon(coupon);
        Users user = tokenService.getUserByToken(token);
        double discountCoupon = 0;

        if(couponUse == null){
            throw new RuntimeException("The coupon does not exist");
        }

        if(!couponUse.isActive() && couponUse.getUsageLimit() <= 0 ){
            throw new RuntimeException("The coupon is not active");
        }

        if(couponUse.getExpirationDate() != null && couponUse.getExpirationDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("The coupon has expired");
        }

        boolean hasUsedCoupon = user.getUsedCoupons().stream().anyMatch(couponUsage -> couponUsage.getCouponId().equals(couponUse.getId()));

        if(hasUsedCoupon) {
            throw new RuntimeException("The coupon has already been used by this user");
        }

        if(price <= couponUse.getMinimumValue()){
            throw new RuntimeException("The total value does not meet the minimum required amount.");
        }

        CouponUsage couponUsage = new CouponUsage();
        couponUsage.setCoupon(couponUse);
        couponUsage.setUser(user);
        couponUsage.setUsedAt(LocalDate.now());
        couponUse.getUsages().add(couponUsage);

        couponUse.setUsedCount(couponUse.getUsedCount() + 1);
        couponUse.setUsageLimit(couponUse.getUsageLimit() - 1);

        UsedCoupon usedCoupon = new UsedCoupon();
        usedCoupon.setCouponId(couponUse.getId());
        usedCoupon.setCoupon(couponUse.getCoupon());
        usedCoupon.setExpirationDate(couponUse.getExpirationDate());
        usedCoupon.setValueDiscount(couponUse.getValueDiscount());
        usedCoupon.setUsedAt(LocalDate.now());
        usedCoupon.setUser(user);

        usedCouponRepository.save(usedCoupon);
        user.getUsedCoupons().add(usedCoupon);

        if(couponUse.getUsageLimit() == 0 || couponUse.getExpirationDate().isBefore(LocalDate.now())){
            couponUse.setActive(false);
        }

        if(couponUse.getDiscountType() == DiscountType.FIXED){
            discountCoupon = couponUse.getValueDiscount();
        }else if(couponUse.getDiscountType() == DiscountType.PERCENT){
            discountCoupon = (price * couponUse.getValueDiscount()) / 100;
        }
        return discountCoupon;
    }

    public List<Coupon> getCouponsUsedByUser(Long id){
        List<Coupon> couponsUsed = new ArrayList<>();
        List<CouponUsage> couponUsages = couponUsageRepository.findByUserId(id);

        for(CouponUsage couponUsage : couponUsages){
            couponsUsed.add(couponUsage.getCoupon());
        }

        return couponsUsed;
    }


}
