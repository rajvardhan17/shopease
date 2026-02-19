package com.shopease.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Cart implements Serializable {
    private String cartId;
    private String userId;
    private Timestamp createdAt;

    public Cart() {}

    public Cart(String cartId, String userId, Timestamp createdAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                '}';
    }
}
