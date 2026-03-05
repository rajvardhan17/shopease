package com.shopease.dao;

import com.shopease.model.Product;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    // ================= PRODUCT MAPPER =================

    private Product mapProduct(ResultSet rs) throws SQLException {

        Product p = new Product();

        p.setId(rs.getString("product_id"));
        p.setTitle(rs.getString("title"));
        p.setShortDescription(rs.getString("short_description"));
        p.setDescription(rs.getString("description"));
        p.setCategoryId(rs.getString("category_id"));
        p.setStatus(rs.getString("status"));
        p.setFeatured(rs.getBoolean("featured"));
        p.setMetadata(rs.getString("metadata"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));

        return p;
    }

    // =====================================================
    // ================= PUBLIC METHODS ====================
    // =====================================================

    // Fetch all ACTIVE products
    public List<Product> getAllProducts() {

        String sql = "SELECT * FROM products WHERE status='ACTIVE' ORDER BY created_at DESC";
        List<Product> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching products", e);
        }

        return list;
    }

    // Fetch product by ID
    public Product getProductById(String productId) {

        String sql = "SELECT * FROM products WHERE product_id=? AND status='ACTIVE'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapProduct(rs);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching product by ID", e);
        }

        return null;
    }

    // Pagination
    public List<Product> getProducts(int page, int size) {

        String sql = """
                SELECT * FROM products
                WHERE status='ACTIVE'
                ORDER BY created_at DESC
                LIMIT ? OFFSET ?
                """;

        List<Product> list = new ArrayList<>();

        int offset = (page - 1) * size;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching paginated products", e);
        }

        return list;
    }

    // Random products
    public List<Product> getRandomProducts(int limit) {

        String sql = """
                SELECT * FROM products
                WHERE status='ACTIVE'
                ORDER BY RAND()
                LIMIT ?
                """;

        List<Product> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching random products", e);
        }

        return list;
    }

    // =====================================================
    // ================= ADMIN CRUD ========================
    // =====================================================

    public boolean addProduct(Product product) {

        String sql = """
                INSERT INTO products
                (product_id, title, short_description, description,
                 category_id, status, featured, metadata, price,
                 image_url, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, product.getTitle());
            stmt.setString(3, product.getShortDescription());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getCategoryId());
            stmt.setString(6, product.getStatus());
            stmt.setBoolean(7, product.isFeatured());
            stmt.setString(8, product.getMetadata());
            stmt.setBigDecimal(9, product.getPrice());
            stmt.setString(10, product.getImageUrl());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product", e);
            return false;
        }
    }

    public boolean updateProduct(Product product) {

        String sql = """
                UPDATE products
                SET title=?, short_description=?, description=?,
                    category_id=?, status=?, featured=?, metadata=?,
                    price=?, image_url=?, updated_at=NOW()
                WHERE product_id=?
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getTitle());
            stmt.setString(2, product.getShortDescription());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCategoryId());
            stmt.setString(5, product.getStatus());
            stmt.setBoolean(6, product.isFeatured());
            stmt.setString(7, product.getMetadata());
            stmt.setBigDecimal(8, product.getPrice());
            stmt.setString(9, product.getImageUrl());
            stmt.setString(10, product.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product", e);
            return false;
        }
    }

    public boolean deleteProduct(String productId) {

        String sql = "DELETE FROM products WHERE product_id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
            return false;
        }
    }
}