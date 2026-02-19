package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.util.DatabaseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/api/search")
public class SearchServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Handle preflight CORS
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String query = request.getParameter("q");
        Map<String, Object> jsonResponse = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();

        if (query == null || query.trim().isEmpty()) {
            jsonResponse.put("success", true);
            jsonResponse.put("results", results);
            objectMapper.writeValue(response.getOutputStream(), jsonResponse);
            return;
        }

        String sql = """
            SELECT p.id, p.title, p.short_description, p.description, c.name AS category,
                   p.metadata
            FROM products p
            LEFT JOIN categories c ON p.category_id = c.id
            WHERE p.title LIKE ? 
               OR p.short_description LIKE ?
               OR p.description LIKE ?
               OR c.name LIKE ?
               OR JSON_EXTRACT(p.metadata, '$.brand') LIKE ?
            LIMIT 50
        """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + query + "%";
            for (int i = 1; i <= 5; i++) stmt.setString(i, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("id", rs.getString("id"));
                    product.put("title", rs.getString("title"));
                    product.put("shortDescription", rs.getString("short_description"));
                    product.put("description", rs.getString("description"));
                    product.put("category", rs.getString("category"));

                    String metadata = rs.getString("metadata");
                    if (metadata != null && !metadata.isEmpty()) {
                        try {
                            Map<String, Object> metaMap = objectMapper.readValue(metadata, Map.class);
                            product.put("brand", metaMap.getOrDefault("brand", ""));
                        } catch (Exception ignored) {}
                    }
                    results.add(product);
                }
            }

            jsonResponse.put("success", true);
            jsonResponse.put("results", results);

        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logger if needed
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("error", "Database error: " + e.getMessage());
        }

        objectMapper.writeValue(response.getOutputStream(), jsonResponse);
    }

    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (origin != null && (origin.equals("http://localhost:3000") || origin.equals("http://localhost:8081"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
    }
}
