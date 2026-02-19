package com.shopease.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class WishListItem implements Serializable {

    private int itemId;      // primary key in wishlist table
    private String userId;      // user owning the wishlist
    private int productId;   // product added to wishlist
    private String name;     // product name
    private BigDecimal price; // product price
    private String imageUrl;  // product image URL

    public WishListItem() {}

    public WishListItem(int itemId, String userId, int productId, String name, BigDecimal price, String imageUrl) {
        this.itemId = itemId;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // ✅ Getters & Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "WishListItem{" +
                "itemId=" + itemId +
                ", userId=" + userId +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
