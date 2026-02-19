package com.shopease.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;                  // UUID
    private String title;
    private String shortDescription;
    private String description;
    private String categoryId;
    private String status;
    private boolean featured;
    private String metadata;            // JSON string
    private BigDecimal price;           // Product price
    private String imageUrl;            // optional image field
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // --- Constructors ---
    public Product() {}

    public Product(String id, String title, String shortDescription, String description,
                   String categoryId, String status, boolean featured, String metadata,
                   BigDecimal price, String imageUrl, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.description = description;
        this.categoryId = categoryId;
        this.status = status;
        this.featured = featured;
        this.metadata = metadata;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    // --- hashCode & equals based on id ---
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return Objects.equals(id, other.id);
    }

    // --- toString for debugging ---
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", status='" + status + '\'' +
                ", featured=" + featured +
                ", metadata='" + metadata + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
