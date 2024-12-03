package com.example.xpress.entities;

import com.example.xpress.views.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Users implements UserDetails {

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
    private String email;

    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private String cpf;

    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private String password;

    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private UserRole role;

    @NotNull(message = NOT_NULL_MESSAGE)
    @JsonView(Views.Public.class)
    private int points;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    @JsonManagedReference
    @JsonView(Views.Internal.class)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    @JsonView(Views.Internal.class)
    private List<Sale> sale;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UsedCoupon> usedCoupons = new ArrayList<>();

    public List<UsedCoupon> getUsedCoupons() {
        return usedCoupons;
    }

    public void setUsedCoupons(List<UsedCoupon> usedCoupons) {
        this.usedCoupons = usedCoupons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN){
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getPassword() {
        return password;
    }

    @NotNull(message = NOT_NULL_MESSAGE)
    public int getPoints() {
        return points;
    }

    public void setPoints(@NotNull(message = NOT_NULL_MESSAGE) int points) {
        this.points = points;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Sale> getSale() {
        return sale;
    }

    public void setSale(List<Sale> sale) {
        this.sale = sale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
