package com.shopease.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    private String variantId;    // UUID
    private String productId;    // UUID of the parent product
    private String variantName;
    private String size;
    private String color;
    private BigDecimal additionalPrice;
    private int stock;
    private String imageUrl;

    // --- Constructors ---
    public ProductVariant() {}

    public ProductVariant(String variantId, String productId, String variantName, String size,
                          String color, BigDecimal additionalPrice, int stock, String imageUrl) {
        this.variantId = variantId;
        this.productId = productId;
        this.variantName = variantName;
        this.size = size;
        this.color = color;
        this.additionalPrice = additionalPrice;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // --- Getters & Setters ---
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public BigDecimal getAdditionalPrice() { return additionalPrice; }
    public void setAdditionalPrice(BigDecimal additionalPrice) { this.additionalPrice = additionalPrice; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // --- hashCode & equals based on variantId ---
    @Override
    public int hashCode() {
        return Objects.hash(variantId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductVariant)) return false;
        ProductVariant other = (ProductVariant) obj;
        return Objects.equals(variantId, other.variantId);
    }

    // --- toString for debugging ---
    @Override
    public String toString() {
        return "ProductVariant{" +
                "variantId='" + variantId + '\'' +
                ", productId='" + productId + '\'' +
                ", variantName='" + variantName + '\'' +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", additionalPrice=" + additionalPrice +
                ", stock=" + stock +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
