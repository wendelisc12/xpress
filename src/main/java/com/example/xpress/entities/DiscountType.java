package com.example.xpress.entities;

public enum DiscountType {
    PERCENT("percent"),
    FIXED("fixed");

    private String discountType;

    DiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountType(){
        return discountType;
    }
}
