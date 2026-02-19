package com.shopease.dao;

import com.shopease.model.Order;
import com.shopease.model.OrderItem;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    // Add a new order
    public boolean addOrder(Order order) {
        String query = "INSERT INTO orders (user_id, status, total_amount, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUserId());
            stmt.setString(2, order.getStatus());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setTimestamp(4, order.getCreatedAt());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) order.setOrderId(keys.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding order", e);
        }
        return false;
    }

    // Get order by ID
    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM orders WHERE order_id = ?";
        Order order = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = mapOrder(rs);
                order.setItems(getOrderItems(orderId)); // fetch items
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching order", e);
        }
        return order;
    }

    // Get orders by user ID
    public List<Order> getOrdersByUserId(String userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(getOrderItems(order.getOrderId()));
                orders.add(order);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching orders", e);
        }
        return orders;
    }

    // ✅ Get all orders (admin)
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY created_at DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(getOrderItems(order.getOrderId())); // fetch items
                orders.add(order);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all orders", e);
        }
        return orders;
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating order status", e);
            return false;
        }
    }

    // Map ResultSet to Order object
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getString("status"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        return order;
    }

    // Fetch order items for an order
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String query = "SELECT * FROM order_items WHERE order_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getBigDecimal("price"));
                items.add(item);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching order items", e);
        }
        return items;
    }
}
