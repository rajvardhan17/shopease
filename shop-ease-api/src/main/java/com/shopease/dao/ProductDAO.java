package com.shopease.dao;

import com.shopease.model.Product;
import com.shopease.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ================= GET ALL PRODUCTS =================

    public List<Product> getAllProducts(int page, int size) {

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
                p.created_at,
                p.updated_at
            FROM products p
            LEFT JOIN categories c 
            ON p.category_id = c.id
            WHERE p.status = 'active'
            LIMIT ? OFFSET ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
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
            e.printStackTrace();
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
            e.printStackTrace();
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
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));

        return product;
    }
}