package com.example.xpress.controllers;

import com.example.xpress.entities.Coupon;
import com.example.xpress.repository.CouponRepository;
import com.example.xpress.repository.CouponUsageRepository;
import com.example.xpress.service.CouponService;
import com.example.xpress.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponUsageRepository couponUsageRepository;

    @Autowired
    CouponService couponService;

    @GetMapping
    @JsonView(Views.Public.class)
    public List<Coupon> getCoupons(){
        List<Coupon> results = couponRepository.findAll();
        return results;
    }

    @PostMapping
    public Coupon createCoupon(@RequestBody Coupon coupon){
        Coupon results = couponService.createCoupon(coupon);
        return results;
    }

    @GetMapping("/{id}")
    public Coupon getCoupon(@PathVariable Long id){
        Coupon results = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("Coupon not found."));
        return results;
    }

    @GetMapping("/user/{id}")
    public List<Coupon> getCouponsUsedByUser(@PathVariable Long id){
        return couponService.getCouponsUsedByUser(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long id){
        couponRepository.deleteById(id);
        return new ResponseEntity<>("Coupon delete with success", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public Coupon atualizeActive(@PathVariable Long id, @RequestParam boolean active){
        Coupon coupon = couponRepository.findById(id).orElseThrow(() -> new RuntimeException("coupon not found."));
        coupon.setActive(active);
        couponRepository.save(coupon);
        return coupon;
    }

}
