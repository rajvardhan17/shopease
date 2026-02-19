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

    // ==================== Product Methods ====================

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
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
                products.add(p);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all products", e);
        }
        return products;
    }

    public Product getProductById(String id) {
        String query = "SELECT * FROM products WHERE id = ?";
        Product product = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getString("id"));
                product.setTitle(rs.getString("title"));
                product.setShortDescription(rs.getString("short_description"));
                product.setDescription(rs.getString("description"));
                product.setCategoryId(rs.getString("category_id"));
                product.setStatus(rs.getString("status"));
                product.setFeatured(rs.getBoolean("featured"));
                product.setMetadata(rs.getString("metadata"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching product by ID", e);
        }

        return product;
    }

    public boolean addProduct(Product product) {
        String query = "INSERT INTO products (id, title, short_description, description, category_id, status, featured, metadata, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getId());
            stmt.setString(2, product.getTitle());
            stmt.setString(3, product.getShortDescription());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getCategoryId());
            stmt.setString(6, product.getStatus());
            stmt.setBoolean(7, product.isFeatured());
            stmt.setString(8, product.getMetadata());
            stmt.setBigDecimal(9, product.getPrice());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding product", e);
        }

        return false;
    }

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

    // ==================== Product Variant Methods ====================

    public List<ProductVariant> getVariantsByProductId(String productId) {
        List<ProductVariant> variants = new ArrayList<>();
        String query = "SELECT * FROM product_variants WHERE product_id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductVariant v = new ProductVariant();
                v.setVariantId(rs.getString("variant_id"));
                v.setProductId(rs.getString("product_id"));
                v.setVariantName(rs.getString("variant_name"));
                v.setSize(rs.getString("size"));
                v.setColor(rs.getString("color"));
                v.setAdditionalPrice(rs.getBigDecimal("additional_price"));
                v.setStock(rs.getInt("stock"));
                v.setImageUrl(rs.getString("image_url"));
                variants.add(v);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching product variants", e);
        }

        return variants;
    }

    public boolean addVariant(ProductVariant variant) {
        String query = "INSERT INTO product_variants (variant_id, product_id, variant_name, size, color, additional_price, stock, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, variant.getVariantId());
            stmt.setString(2, variant.getProductId());
            stmt.setString(3, variant.getVariantName());
            stmt.setString(4, variant.getSize());
            stmt.setString(5, variant.getColor());
            stmt.setBigDecimal(6, variant.getAdditionalPrice());
            stmt.setInt(7, variant.getStock());
            stmt.setString(8, variant.getImageUrl());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding variant", e);
        }

        return false;
    }

    public boolean updateVariant(ProductVariant variant) {
        String query = "UPDATE product_variants SET variant_name=?, size=?, color=?, additional_price=?, stock=?, image_url=? WHERE variant_id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, variant.getVariantName());
            stmt.setString(2, variant.getSize());
            stmt.setString(3, variant.getColor());
            stmt.setBigDecimal(4, variant.getAdditionalPrice());
            stmt.setInt(5, variant.getStock());
            stmt.setString(6, variant.getImageUrl());
            stmt.setString(7, variant.getVariantId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating variant", e);
        }

        return false;
    }

    public boolean deleteVariant(String variantId) {
        String query = "DELETE FROM product_variants WHERE variant_id=?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, variantId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting variant", e);
        }

        return false;
    }
}
