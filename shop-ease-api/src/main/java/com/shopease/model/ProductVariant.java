package com.shopease.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class ProductVariant implements Serializable {

    private static final long serialVersionUID = 1L;

    // ================= Fields =================

    private String variantId;       // UUID
    private String productId;       // Parent product UUID

    private String variantName;
    private String size;
    private String color;

    private BigDecimal additionalPrice = BigDecimal.ZERO;

    private int stock;
    private String imageUrl;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ================= Constructors =================

    public ProductVariant() {
    }

    // Constructor for creating a new variant
    public ProductVariant(String productId,
                          String variantName,
                          String size,
                          String color,
                          BigDecimal additionalPrice,
                          int stock,
                          String imageUrl) {

        this.productId = productId;
        this.variantName = variantName;
        this.size = size;
        this.color = color;
        this.additionalPrice = additionalPrice != null ? additionalPrice : BigDecimal.ZERO;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // Full constructor
    public ProductVariant(String variantId,
                          String productId,
                          String variantName,
                          String size,
                          String color,
                          BigDecimal additionalPrice,
                          int stock,
                          String imageUrl,
                          Timestamp createdAt,
                          Timestamp updatedAt) {

        this.variantId = variantId;
        this.productId = productId;
        this.variantName = variantName;
        this.size = size;
        this.color = color;
        this.additionalPrice = additionalPrice != null ? additionalPrice : BigDecimal.ZERO;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ================= Getters & Setters =================

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice != null ? additionalPrice : BigDecimal.ZERO;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ================= Utility Methods =================

    public boolean isInStock() {
        return stock > 0;
    }

    // ================= equals & hashCode =================

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof ProductVariant)) return false;

        ProductVariant that = (ProductVariant) o;

        return variantId != null && variantId.equals(that.variantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variantId);
    }

    // ================= toString =================

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