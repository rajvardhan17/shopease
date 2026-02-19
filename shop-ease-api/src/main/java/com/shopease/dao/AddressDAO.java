package com.shopease.dao;

import com.shopease.model.Address;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    // =========================
    // Get all addresses of a user
    // =========================
    public List<Address> getAddressesByUserId(String userId) {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM user_addresses WHERE user_id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Address addr = new Address();
                addr.setAddressId(rs.getString("id"));
                addr.setUserId(rs.getString("user_id"));
                addr.setFullName(rs.getString("recipient_name"));
                addr.setPhone(rs.getString("phone"));
                addr.setAddressLine1(rs.getString("line1"));
                addr.setAddressLine2(rs.getString("line2"));
                addr.setCity(rs.getString("city"));
                addr.setState(rs.getString("state"));
                addr.setPostalCode(rs.getString("postal_code"));
                addr.setCountry(rs.getString("country"));
                addr.setDefault(rs.getBoolean("is_default"));
                addresses.add(addr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    // =========================
    // Add new address
    // =========================
    public boolean addAddress(Address address) {
        String sql = "INSERT INTO user_addresses(user_id, recipient_name, phone, line1, line2, city, state, postal_code, country, is_default) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address.getUserId());
            ps.setString(2, address.getFullName());
            ps.setString(3, address.getPhone());
            ps.setString(4, address.getAddressLine1());
            ps.setString(5, address.getAddressLine2());
            ps.setString(6, address.getCity());
            ps.setString(7, address.getState());
            ps.setString(8, address.getPostalCode());
            ps.setString(9, address.getCountry());
            ps.setBoolean(10, address.isDefault());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // Update address
    // =========================
    public boolean updateAddress(Address address) {
        String findIdSql = "SELECT id FROM user_addresses WHERE user_id = ? AND is_default = TRUE LIMIT 1";
        String updateSql = "UPDATE user_addresses SET recipient_name=?, phone=?, line1=?, line2=?, city=?, state=?, postal_code=?, country=?, is_default=? WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection()) {

            // Use existing addressId if provided
            String addressId = address.getAddressId();

            // If not provided, fetch default address for the user
            if (addressId == null || addressId.isEmpty()) {
                try (PreparedStatement psFind = conn.prepareStatement(findIdSql)) {
                    psFind.setString(1, address.getUserId());
                    try (ResultSet rs = psFind.executeQuery()) {
                        if (rs.next()) {
                            addressId = rs.getString("id");
                            System.out.println("✅ Found address_id: " + addressId);
                        } else {
                            System.out.println("⚠️ No address found for user: " + address.getUserId());
                            return false;
                        }
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, address.getFullName());
                ps.setString(2, address.getPhone());
                ps.setString(3, address.getAddressLine1());
                ps.setString(4, address.getAddressLine2());
                ps.setString(5, address.getCity());
                ps.setString(6, address.getState());
                ps.setString(7, address.getPostalCode());
                ps.setString(8, address.getCountry());
                ps.setBoolean(9, address.isDefault());
                ps.setString(10, addressId);

                int rows = ps.executeUpdate();
                System.out.println("🔧 Rows updated: " + rows);
                return rows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // =========================
    // Delete address
    // =========================
    public boolean deleteAddress(String addressId) {
        String sql = "DELETE FROM user_addresses WHERE id=?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, addressId);
            int rows = ps.executeUpdate();
            System.out.println("🔧 Rows deleted: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
