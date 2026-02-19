package com.shopease.dao;

import com.shopease.model.WishListItem;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WishListDAO {
    private static final Logger LOGGER = Logger.getLogger(WishListDAO.class.getName());

    // Add item to wishlist by userId and productId
    public boolean addToWishlist(String userId, int productId) {
        String query = "INSERT INTO wishlist (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, userId);
            stmt.setInt(2, productId);

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding to wishlist", e);
        }
        return false;
    }

    // Get all wishlist items for a user
    public List<WishListItem> getWishlistItemsByUserId(String userId) {
        List<WishListItem> items = new ArrayList<>();
        String query = "SELECT w.item_id, w.user_id, w.product_id, p.name, p.price, p.image_url " +
                "FROM wishlist w JOIN products p ON w.product_id = p.product_id " +
                "WHERE w.user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WishListItem item = new WishListItem();
                item.setItemId(rs.getInt("item_id"));
                item.setUserId(rs.getString("user_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setImageUrl(rs.getString("image_url"));
                items.add(item);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching wishlist items", e);
        }
        return items;
    }

    // Remove item from wishlist
    public boolean removeFromWishlist(int itemId) {
        String query = "DELETE FROM wishlist WHERE item_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing wishlist item", e);
        }
        return false;
    }
}
