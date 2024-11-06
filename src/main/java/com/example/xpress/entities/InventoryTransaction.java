package com.example.xpress.entities;

import com.example.xpress.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Internal.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonView(Views.Internal.class)
    private Product product;

    @Enumerated(EnumType.STRING)
    @JsonView(Views.Internal.class)
    private MovimentType movimentType;

    @JsonView(Views.Internal.class)
    private LocalDateTime movimentDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(Views.Internal.class)
    private Users user;

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public MovimentType getMovimentType() {
        return movimentType;
    }

    public void setMovimentType(MovimentType movimentType) {
        this.movimentType = movimentType;
    }

    public LocalDateTime getMovimentDate() {
        return movimentDate;
    }

    public void setMovimentDate(LocalDateTime movimentDate) {
        this.movimentDate = movimentDate;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
