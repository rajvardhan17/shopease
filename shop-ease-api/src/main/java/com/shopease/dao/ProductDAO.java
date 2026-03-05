package com.shopease.dao;

import com.shopease.model.Product;
import com.shopease.model.ProductVariant;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    // =====================================================
    // ================= PRODUCT MAPPER ====================
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
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));

        return p;
    }

    // =====================================================
    // ================= VARIANT MAPPER ====================
    // =====================================================

    private ProductVariant mapVariant(ResultSet rs) throws SQLException {

        ProductVariant v = new ProductVariant();

        v.setVariantId(rs.getString("variant_id"));
        v.setProductId(rs.getString("product_id"));
        v.setVariantName(rs.getString("variant_name"));
        v.setSize(rs.getString("size"));
        v.setColor(rs.getString("color"));
        v.setAdditionalPrice(rs.getBigDecimal("additional_price"));
        v.setStock(rs.getInt("stock"));
        v.setImageUrl(rs.getString("image_url"));

        return v;
    }

    // =====================================================
    // ================= PRODUCT METHODS ===================
    // =====================================================

    public List<Product> getAllProducts() {

        String sql = "SELECT * FROM products WHERE status='active' ORDER BY created_at DESC";

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

    // -----------------------------------------------------

    public Product getProductById(String id) {

        String sql = "SELECT * FROM products WHERE id=? AND status='active'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

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

    // -----------------------------------------------------

    public List<Product> getProducts(int page, int size) {

        String sql = """
                SELECT * FROM products
                WHERE status='active'
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

    // -----------------------------------------------------

    public List<Product> getRandomProducts(int limit) {

        String sql = """
                SELECT * FROM products
                WHERE status='active'
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
    // ================= ADMIN PRODUCT CRUD =================
    // =====================================================

    public boolean addProduct(Product product) {

        String sql = """
                INSERT INTO products
                (id, title, short_description, description,
                 category_id, status, featured, metadata,
                 price, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
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

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product", e);
            return false;
        }
    }

    // -----------------------------------------------------

    public boolean updateProduct(Product product) {

        String sql = """
                UPDATE products
                SET title=?, short_description=?, description=?,
                    category_id=?, status=?, featured=?,
                    metadata=?, price=?, updated_at=NOW()
                WHERE id=?
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
            stmt.setString(9, product.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product", e);
            return false;
        }
    }

    // -----------------------------------------------------

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
    // ================= VARIANT METHODS ===================
    // =====================================================

    public List<ProductVariant> getVariantsByProductId(String productId) {

        String sql = """
                SELECT * FROM product_variants
                WHERE product_id=?
                ORDER BY variant_name
                """;

        List<ProductVariant> variants = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    variants.add(mapVariant(rs));
                }

            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching variants", e);
        }

        return variants;
    }
}