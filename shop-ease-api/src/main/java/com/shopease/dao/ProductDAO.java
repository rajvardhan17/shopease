package com.shopease.dao;

import com.shopease.model.Product;
import com.shopease.model.ProductVariant;
import com.shopease.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    // ================= GET ALL PRODUCTS =================
    public List<Product> getAllProducts(int page, int size) {
        size = 15;
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = """
              SELECT 
                p.id,
                p.title,
                p.short_description,
                p.description,
                c.name AS category,
                p.status,
                p.featured,
                p.metadata,
                p.price,
                (
                    SELECT pi.url 
                    FROM product_images pi 
                    WHERE pi.product_id = p.id 
                    ORDER BY pi.sort_order ASC 
                    LIMIT 1
                ) AS image_url,
                p.created_at,
                p.updated_at
            FROM products p
            LEFT JOIN categories c 
                ON p.category_id = c.id
            WHERE p.status = 'active'
            LIMIT ? OFFSET ?
        """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            // ✅ ADD THIS DEBUG
            System.out.println("DB URL: " + conn.getMetaData().getURL());
            System.out.println("DB Name: " + conn.getCatalog());

            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Product found: " + rs.getString("title")); // extra debug
                products.add(mapProduct(rs));
            }

        }catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching all products", e);
        }

        return products;
    }

    public List<ProductVariant> getVariantsByProductId(String productId) {
        List<ProductVariant> variants = new ArrayList<>();
        String sql = "SELECT * FROM product_variants WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductVariant variant = new ProductVariant();
                variant.setVariantId(rs.getString("variant_id"));
                variant.setProductId(rs.getString("product_id"));
                variant.setVariantName(rs.getString("variant_name"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setAdditionalPrice(rs.getBigDecimal("additional_price"));
                variant.setStock(rs.getInt("stock"));
                variant.setImageUrl(rs.getString("image_url"));
                variants.add(variant);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching product variants", e);
        }

        return variants;
    }

    // ================= RANDOM PRODUCTS =================
    public List<Product> getRandomProducts(int limit) {
        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT 
                p.id,
                p.title,
                p.short_description,
                p.description,
                c.name AS category,
                p.status,
                p.featured,
                p.metadata,
                p.price,
                (
                    SELECT pi.url 
                    FROM product_images pi 
                    WHERE pi.product_id = p.id 
                    ORDER BY pi.sort_order ASC 
                    LIMIT 1
                ) AS image_url,
                p.created_at,
                p.updated_at
            FROM products p
            LEFT JOIN categories c 
                ON p.category_id = c.id
            WHERE p.status='active'
            ORDER BY RAND()
            LIMIT ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching random products", e);
        }

        return products;
    }

    // ================= FEATURED PRODUCTS =================
    public List<Product> getFeaturedProducts(int limit) {
        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT 
                p.id,
                p.title,
                p.short_description,
                p.description,
                c.name AS category,
                p.status,
                p.featured,
                p.metadata,
                p.price,
                (
                    SELECT pi.url 
                    FROM product_images pi 
                    WHERE pi.product_id = p.id 
                    ORDER BY pi.sort_order ASC 
                    LIMIT 1
                ) AS image_url,
                p.created_at,
                p.updated_at
            FROM products p
            LEFT JOIN categories c 
                ON p.category_id = c.id
            WHERE p.status='active' 
            AND p.featured = 1
            LIMIT ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching featured products", e);
        }

        return products;
    }

    // ================= MAP RESULTSET =================
    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();

        product.setId(rs.getString("id"));
        product.setTitle(rs.getString("title"));
        product.setShortDescription(rs.getString("short_description"));
        product.setDescription(rs.getString("description"));
        product.setCategory(rs.getString("category"));
        product.setStatus(rs.getString("status"));
        product.setFeatured(rs.getBoolean("featured"));
        product.setMetadata(rs.getString("metadata"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setImageUrl(rs.getString("image_url"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));

        return product;
    }

    // ================= ADD VARIANT =================
    public boolean addVariant(ProductVariant variant) {
        String sql = """
            INSERT INTO product_variants
            (variant_id, product_id, variant_name, size, color,
             additional_price, stock, image_url)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
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

    // ================= UPDATE VARIANT =================
    public boolean updateVariant(ProductVariant variant) {
        String sql = """
            UPDATE product_variants
            SET variant_name=?, size=?, color=?,
                additional_price=?, stock=?, image_url=?
            WHERE variant_id=?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
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

    // ================= DELETE VARIANT =================
    public boolean deleteVariant(String variantId) {
        String sql = "DELETE FROM product_variants WHERE variant_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, variantId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting variant", e);
            return false;
        }
    }
}
