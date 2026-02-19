package com.shopease.dao;

import com.shopease.model.Category;
import com.shopease.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryDAO {
    private static final Logger LOGGER = Logger.getLogger(CategoryDAO.class.getName());

    public boolean addCategory(Category category) {
        String query = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding category", e);
            return false;
        }
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM categories";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
        }
        return categories;
    }

    public Category getCategoryById(int id) {
        String query = "SELECT * FROM categories WHERE category_id = ?";
        Category category = null;
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching category by ID", e);
        }
        return category;
    }

    public boolean updateCategory(Category category) {
        String query = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating category", e);
            return false;
        }
    }

    public boolean deleteCategory(int id) {
        String query = "DELETE FROM categories WHERE category_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting category", e);
            return false;
        }
    }
}
