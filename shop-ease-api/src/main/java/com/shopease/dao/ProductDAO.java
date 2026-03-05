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
    // PRODUCT MAPPER
    // =====================================================

    private Product mapProduct(ResultSet rs) throws SQLException {

        Product p = new Product();

        p.setId(rs.getString("id"));
        p.setTitle(rs.getString("title"));
        p.setShortDescription(rs.getString("short_description"));
        p.setDescription(rs.getString("description"));
        p.setCategory(rs.getString("category")); // category name
        p.setStatus(rs.getString("status"));
        p.setFeatured(rs.getBoolean("featured"));
        p.setMetadata(rs.getString("metadata"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));

        return p;
    }

    // =====================================================
    // VARIANT MAPPER
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
    // GET ALL PRODUCTS
    // =====================================================

    public List<Product> getAllProducts() {

        String sql = """
        SELECT p.*, c.name AS category
        FROM products p
        JOIN categories c ON p.category_id = c.id
        WHERE p.status='active'
        ORDER BY p.created_at DESC
        """;

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

    // =====================================================
    // GET PRODUCT BY ID
    // =====================================================

    public Product getProductById(String id) {

        String sql = """
        SELECT p.*, c.name AS category
        FROM products p
        JOIN categories c ON p.category_id = c.id
        WHERE p.id=? AND p.status='active'
        """;

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

    // =====================================================
    // RANDOM PRODUCTS
    // =====================================================

    public List<Product> getRandomProducts(int limit) {

        String sql = """
        SELECT p.*, c.name AS category
        FROM products p
        JOIN categories c ON p.category_id = c.id
        WHERE p.status='active'
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
    // ADD PRODUCT
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
            stmt.setString(5, product.getCategory()); // category_id
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

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

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
    // GET VARIANTS
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

    // =====================================================
    // ADD VARIANT
    // =====================================================

    public boolean addVariant(ProductVariant variant) {

        String sql = """
        INSERT INTO product_variants
        (variant_id, product_id, variant_name, size, color,
         additional_price, stock, image_url)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, variant.getProductId());
            stmt.setString(3, variant.getVariantName());
            stmt.setString(4, variant.getSize());
            stmt.setString(5, variant.getColor());
            stmt.setBigDecimal(6, variant.getAdditionalPrice());
            stmt.setInt(7, variant.getStock());
            stmt.setString(8, variant.getImageUrl());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding variant", e);
            return false;
        }
    }

    // =====================================================
    // UPDATE VARIANT
    // =====================================================

    public boolean updateVariant(ProductVariant variant) {

        String sql = """
        UPDATE product_variants
        SET variant_name=?, size=?, color=?,
            additional_price=?, stock=?, image_url=?
        WHERE variant_id=?
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, variant.getVariantName());
            stmt.setString(2, variant.getSize());
            stmt.setString(3, variant.getColor());
            stmt.setBigDecimal(4, variant.getAdditionalPrice());
            stmt.setInt(5, variant.getStock());
            stmt.setString(6, variant.getImageUrl());
            stmt.setString(7, variant.getVariantId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating variant", e);
            return false;
        }
    }

    // =====================================================
    // DELETE VARIANT
    // =====================================================

    public boolean deleteVariant(String variantId) {

        String sql = "DELETE FROM product_variants WHERE variant_id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, variantId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting variant", e);
            return false;
        }
    }
}