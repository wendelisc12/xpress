package com.example.xpress.entities;

public enum PaymentMethodEnum {
    CREDIT_CARD("credit card"),
    DEBIT_CARD("debit card"),
    MONEY("money");

    private String payment;

    PaymentMethodEnum(String payment){
        this.payment = payment;
    }

    public String getPaymentMethod(){
        return this.payment;
    }
}
