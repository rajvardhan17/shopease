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

    // =====================================================
    // ================= PRODUCT SECTION ===================
    // =====================================================

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
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products ORDER BY created_at DESC";
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

    public List<Product> getRandomProducts(int limit) {
        String sql = "SELECT * FROM products ORDER BY RAND() LIMIT ?";
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

    public List<Product> getProductsByCategory(String categoryId, int page, int size) {
        String sql = "SELECT * FROM products WHERE category_id=? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Product> list = new ArrayList<>();
        int offset = (page - 1) * size;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoryId);
            stmt.setInt(2, size);
            stmt.setInt(3, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching category products", e);
        }

        return list;
    }

    public Product getProductById(String id) {
        String sql = "SELECT * FROM products WHERE id=?";
        Product product = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = mapProduct(rs);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching product by ID", e);
        }

        return product;
    }

    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (title, short_description, description, category_id, status, featured, metadata, price, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product", e);
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET title=?, short_description=?, description=?, category_id=?, status=?, featured=?, metadata=?, price=?, image_url=? WHERE id=?";

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

    public boolean deleteProduct(String id) {
        String sql = "DELETE FROM products WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
            return false;
        }
    }

    // =====================================================
    // ================= VARIANT SECTION ===================
    // =====================================================

    private ProductVariant mapVariant(ResultSet rs) throws SQLException {
        ProductVariant v = new ProductVariant();
        v.setId(rs.getString("id"));
        v.setProductId(rs.getString("product_id"));
        v.setSize(rs.getString("size"));
        v.setColor(rs.getString("color"));
        v.setStock(rs.getInt("stock"));
        v.setPrice(rs.getBigDecimal("price"));
        return v;
    }

    public List<ProductVariant> getVariantsByProductId(String productId) {
        String sql = "SELECT * FROM product_variants WHERE product_id=?";
        List<ProductVariant> list = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapVariant(rs));
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching variants", e);
        }

        return list;
    }

    public boolean addVariant(ProductVariant variant) {
        String sql = "INSERT INTO product_variants (product_id, size, color, stock, price) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, variant.getProductId());
            stmt.setString(2, variant.getSize());
            stmt.setString(3, variant.getColor());
            stmt.setInt(4, variant.getStock());
            stmt.setBigDecimal(5, variant.getPrice());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding variant", e);
            return false;
        }
    }

    public boolean updateVariant(ProductVariant variant) {
        String sql = "UPDATE product_variants SET size=?, color=?, stock=?, price=? WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, variant.getSize());
            stmt.setString(2, variant.getColor());
            stmt.setInt(3, variant.getStock());
            stmt.setBigDecimal(4, variant.getPrice());
            stmt.setString(5, variant.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating variant", e);
            return false;
        }
    }

    public boolean deleteVariant(String id) {
        String sql = "DELETE FROM product_variants WHERE id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting variant", e);
            return false;
        }
    }
}