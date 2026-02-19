package com.shopease.dao;

import com.shopease.model.User;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAO {

    // Fetch user by email
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        try (Connection conn = DatabaseUtil.getConnection()) {
            assert conn != null;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    User u = new User(); // UUID as String
                    u.setUserId(rs.getString("id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setPassword(rs.getString("password_hash"));
                    return u;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Register a new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users(full_name, email, password_hash, phone) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Generate UUID for user_id
            String uuid = UUID.randomUUID().toString();
            user.setUserId(uuid);

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Admin: Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getString("id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setRole(rs.getString("role"));
                users.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // ✅ Admin: Update user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET full_name=?, email=?, phone=? WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getUserId()); // UUID as String

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Admin: Delete user
    public boolean deleteUser(String userId) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
