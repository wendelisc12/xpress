package com.example.xpress.entities;

import com.example.xpress.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Products")
public class Product {

    private static final String NOT_NULL_MESSAGE = "Please fill in all fields correctly.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;

    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private String name;

    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private Float price;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private Category category;

    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private int qttStock;

    @JsonView(Views.Public.class)
    private LocalDate expirationDate;

    @JsonView(Views.Public.class)
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

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
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
