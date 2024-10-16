package com.example.xpress.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "Products")
public class Product {

    private static final String NOT_NULL_MESSAGE = "Please fill in all fields correctly.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = NOT_NULL_MESSAGE)
    private String name;
    @NotNull(message = NOT_NULL_MESSAGE)
    private Float price;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @NotNull(message = NOT_NULL_MESSAGE)
    private Category category;

    @NotNull(message = NOT_NULL_MESSAGE)
    private int qttStock;
    private Date expirationDate;
    @NotNull(message = NOT_NULL_MESSAGE)
    private boolean perishable;

    public Product() {
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQttStock() {
        return qttStock;
    }

    public void setQttStock(int qttStock) {
        this.qttStock = qttStock;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isPerishable() {
        return perishable;
    }

    public void setPerishable(boolean perishable) {
        this.perishable = perishable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
