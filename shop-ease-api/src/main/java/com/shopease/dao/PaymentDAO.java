package com.shopease.dao;

import com.shopease.model.Payment;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentDAO {
    private static final Logger LOGGER = Logger.getLogger(PaymentDAO.class.getName());

    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, payment_method, payment_status, transaction_id, amount, payment_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getPaymentMethod());
            ps.setString(3, payment.getPaymentStatus());
            ps.setString(4, payment.getTransactionId());
            ps.setBigDecimal(5, payment.getAmount());
            ps.setTimestamp(6, payment.getPaymentDate());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding payment", e);
            return false;
        }
    }

    public Payment getPaymentByOrderId(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        Payment payment = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setPaymentStatus(rs.getString("payment_status"));
                payment.setTransactionId(rs.getString("transaction_id"));
                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching payment", e);
        }
        return payment;
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setPaymentStatus(rs.getString("payment_status"));
                payment.setTransactionId(rs.getString("transaction_id"));
                payment.setAmount(rs.getBigDecimal("amount"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
                list.add(payment);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching payments", e);
        }
        return list;
    }
}
