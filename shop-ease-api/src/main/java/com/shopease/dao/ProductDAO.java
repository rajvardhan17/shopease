package com.shopease.dao;

import com.shopease.model.Product;
import com.shopease.model.ProductVariant;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    // ==================== Utility Mapper ====================

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getString("id"));
        p.setTitle(rs.getString("title"));
        p.setShortDescription(rs.getString("short_description"));
        p.setDescription(rs.getString("description"));
        p.setCategoryId(rs.getString("category_id"));
        p.setStatus(rs.getString("status"));
        p.setFeatured(rs.getBoolean("featured"));
        p.setMetadata(rs.getString("metadata"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    // ==================== Get All ====================

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products ORDER BY created_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all products", e);
        }

        return products;
    }

    // ==================== Random 10 ====================

    public List<Product> getRandomProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products ORDER BY RAND() LIMIT ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching random products", e);
        }

        return products;
    }

    // ==================== Pagination ====================

    public List<Product> getProductsWithPagination(int page, int size) {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;

        String query = "SELECT * FROM products ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching paginated products", e);
        }

        return products;
    }

    // ==================== Category Filter ====================

    public List<Product> getProductsByCategory(String categoryId, int page, int size) {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;

        String query = "SELECT * FROM products WHERE category_id=? ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryId);
            stmt.setInt(2, size);
            stmt.setInt(3, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching category products", e);
        }

        return products;
    }

    // ==================== Get By ID ====================

    public Product getProductById(String id) {
        String query = "SELECT * FROM products WHERE id = ?";
        Product product = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                product = mapProduct(rs);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching product by ID", e);
        }

        return product;
    }

    // ==================== Add Product (NO manual ID) ====================

    public boolean addProduct(Product product) {

        String query = "INSERT INTO products (title, short_description, description, category_id, status, featured, metadata, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getTitle());
            stmt.setString(2, product.getShortDescription());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCategoryId());
            stmt.setString(5, product.getStatus());
            stmt.setBoolean(6, product.isFeatured());
            stmt.setString(7, product.getMetadata());
            stmt.setBigDecimal(8, product.getPrice());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding product", e);
        }

        return false;
    }

    // ==================== Update ====================

    public boolean updateProduct(Product product) {

        String query = "UPDATE products SET title=?, short_description=?, description=?, category_id=?, status=?, featured=?, metadata=?, price=? WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getTitle());
            stmt.setString(2, product.getShortDescription());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCategoryId());
            stmt.setString(5, product.getStatus());
            stmt.setBoolean(6, product.isFeatured());
            stmt.setString(7, product.getMetadata());
            stmt.setBigDecimal(8, product.getPrice());
            stmt.setString(9, product.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating product", e);
        }

        return false;
    }

    // ==================== Delete ====================

    public boolean deleteProduct(String id) {

        String query = "DELETE FROM products WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
        }

        return false;
    }
}