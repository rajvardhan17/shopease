package com.shopease.dao;

import com.shopease.model.Cart;
import com.shopease.model.CartItem;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartDAO {
    private static final Logger LOGGER = Logger.getLogger(CartDAO.class.getName());

    // Get cart by userId, create if not exists
    public Cart getCartByUserId(String userId) {
        String sql = "SELECT * FROM cart WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getString("cart_id")); // String UUID
                cart.setUserId(rs.getString("user_id"));
                cart.setCreatedAt(rs.getTimestamp("created_at"));
                return cart;
            } else {
                String newCartId = createCartForUser(userId);
                if (newCartId != null) {
                    Cart newCart = new Cart();
                    newCart.setCartId(newCartId);
                    newCart.setUserId(userId);
                    return newCart;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting cart for user: " + userId, e);
        }
        return null;
    }

    // Create cart for a user
    private String createCartForUser(String userId) {
        String sql = "INSERT INTO cart (user_id) VALUES (?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, userId);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) return keys.getString(1);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating cart for user: " + userId, e);
        }
        return null;
    }

    // Get all items in a cart
    public List<CartItem> getCartItems(String cartId) {
        List<CartItem> items = new ArrayList<>();
        String sql = """
                SELECT ci.cart_item_id, ci.cart_id, ci.product_id, ci.variant_id, ci.quantity,
                       p.product_name, p.price, p.image_url
                FROM cart_items ci
                JOIN products p ON ci.product_id = p.product_id
                WHERE ci.cart_id = ?
                """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cartId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getString("cart_item_id")); // String
                item.setCartId(rs.getString("cart_id"));
                item.setProductId(rs.getString("product_id"));
                item.setVariantId(rs.getObject("variant_id") != null ? rs.getString("variant_id") : null);
                item.setQuantity(rs.getInt("quantity"));
                item.setProductName(rs.getString("product_name"));
                item.setProductPrice(rs.getBigDecimal("price"));
                item.setImageUrl(rs.getString("image_url"));
                items.add(item);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting cart items for cartId: " + cartId, e);
        }
        return items;
    }

    // Add item to cart (or update quantity if exists)
    public boolean addItemToCart(CartItem item) {
        String checkSql = "SELECT cart_item_id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ? AND (variant_id = ? OR (variant_id IS NULL AND ? IS NULL))";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, item.getCartId());
            checkStmt.setString(2, item.getProductId());

            if (item.getVariantId() != null) {
                checkStmt.setString(3, item.getVariantId());
                checkStmt.setString(4, item.getVariantId());
            } else {
                checkStmt.setNull(3, Types.INTEGER);
                checkStmt.setNull(4, Types.INTEGER);
            }

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                String existingItemId = rs.getString("cart_item_id");
                int existingQty = rs.getInt("quantity");
                return updateQuantity(existingItemId, existingQty + item.getQuantity());
            }

            String insertSql = "INSERT INTO cart_items (cart_id, product_id, variant_id, quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, item.getCartId());
                insertStmt.setString(2, item.getProductId());
                if (item.getVariantId() != null) insertStmt.setString(3, item.getVariantId());
                else insertStmt.setNull(3, Types.INTEGER);
                insertStmt.setInt(4, item.getQuantity());
                return insertStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding item to cart", e);
        }
        return false;
    }

    // Update cart item quantity
    public boolean updateQuantity(String cartItemId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setString(2, cartItemId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating quantity for cart_item_id: " + cartItemId, e);
        }
        return false;
    }

    // Remove item from cart
    public boolean removeItem(String cartItemId) {
        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cartItemId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing cart item: " + cartItemId, e);
        }
        return false;
    }

    // Clear all items in a cart
    public boolean clearCart(String cartId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cartId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error clearing cart: " + cartId, e);
        }
        return false;
    }

    // Calculate cart total
    public double getCartTotal(String cartId) {
        String sql = """
                SELECT SUM(ci.quantity * p.price) AS total
                FROM cart_items ci
                JOIN products p ON ci.product_id = p.product_id
                WHERE ci.cart_id = ?
                """;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cartId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("total");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating cart total for cartId: " + cartId, e);
        }
        return 0.0;
    }
}
