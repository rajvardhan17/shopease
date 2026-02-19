package com.shopease.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {
    private String cartItemId;
    private String cartId;
    private String productId;
    private String variantId; // Nullable for products without variants
    private int quantity;

    // Additional fields from products table join
    private String productName;
    private BigDecimal productPrice;
    private String imageUrl;

    public CartItem() {}

    public CartItem(String cartItemId,String cartId, String productId, String variantId, Integer quantity) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
    }

    // Getters & Setters
    public String getCartItemId() { return cartItemId; }
    public void setCartItemId(String cartItemId) { this.cartItemId = cartItemId; }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", cartId=" + cartId +
                ", productId=" + productId +
                ", variantId=" + variantId +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
