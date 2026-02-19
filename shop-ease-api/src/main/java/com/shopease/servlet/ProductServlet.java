package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.ProductDAO;
import com.shopease.model.Product;
import com.shopease.model.ProductVariant;
import com.shopease.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/products/*")
public class ProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());
    private final ProductDAO productDAO = new ProductDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    // ===== CORS helper =====
    private void setCorsHeaders(HttpServletRequest req, HttpServletResponse resp) {
        String[] allowedOrigins = {
                "http://localhost:8080",
                "http://localhost:3000",
                "http://192.168.56.1:8080",
                "http://192.168.1.3:8080"
        };
        String origin = req.getHeader("Origin");
        if (origin != null) {
            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equals(origin)) {
                    resp.setHeader("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        resp.setHeader("Vary", "Origin");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean isAdmin(HttpSession session) {
        return session != null && session.getAttribute("user") != null
                && "admin".equals(((User) session.getAttribute("user")).getRole());
    }

    // ===== GET products / single product / variants =====
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.put("success", false);
            result.put("message", "Login required");
            mapper.writeValue(resp.getOutputStream(), result);
            return;
        }

        try {
            String pathInfo = req.getPathInfo(); // e.g., / or /{id} or /{id}/variants

            if (pathInfo == null || pathInfo.equals("/")) {
                // Fetch all products
                List<Product> products = productDAO.getAllProducts();
                result.put("success", true);
                result.put("products", products);
            } else {
                String[] parts = pathInfo.substring(1).split("/");

                String productId = parts[0];
                Product product = productDAO.getProductById(productId);

                if (product == null) {
                    // Product not found
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Product not found");
                } else if (parts.length == 2 && "variants".equals(parts[1])) {
                    // Fetch variants for a product
                    List<ProductVariant> variants = productDAO.getVariantsByProductId(productId);
                    result.put("success", true);
                    result.put("product", product);
                    result.put("variants", variants);
                } else {
                    // Single product
                    result.put("success", true);
                    result.put("product", product);
                    // Optional: fetch variants too
                    List<ProductVariant> variants = productDAO.getVariantsByProductId(productId);
                    result.put("variants", variants);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching products", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error fetching products");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ===== POST: add product / variant =====
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            result.put("success", false);
            result.put("message", "Admins only");
            mapper.writeValue(resp.getOutputStream(), result);
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if ("/variant".equals(pathInfo)) {
                ProductVariant variant = mapper.readValue(req.getInputStream(), ProductVariant.class);
                boolean added = productDAO.addVariant(variant);
                result.put("success", added);
                result.put("message", added ? "Variant added successfully" : "Failed to add variant");
            } else {
                Product product = mapper.readValue(req.getInputStream(), Product.class);
                boolean added = productDAO.addProduct(product);
                result.put("success", added);
                result.put("message", added ? "Product added successfully" : "Failed to add product");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product/variant", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error adding product/variant");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ===== PUT: update product / variant =====
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            result.put("success", false);
            result.put("message", "Admins only");
            mapper.writeValue(resp.getOutputStream(), result);
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if ("/variant".equals(pathInfo)) {
                ProductVariant variant = mapper.readValue(req.getInputStream(), ProductVariant.class);
                boolean updated = productDAO.updateVariant(variant);
                result.put("success", updated);
                result.put("message", updated ? "Variant updated successfully" : "Failed to update variant");
            } else {
                Product product = mapper.readValue(req.getInputStream(), Product.class);
                boolean updated = productDAO.updateProduct(product);
                result.put("success", updated);
                result.put("message", updated ? "Product updated successfully" : "Failed to update product");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product/variant", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error updating product/variant");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ===== DELETE: product / variant =====
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            result.put("success", false);
            result.put("message", "Admins only");
            mapper.writeValue(resp.getOutputStream(), result);
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "ID required for deletion");
            } else {
                String[] parts = pathInfo.substring(1).split("/");
                if (parts.length == 2 && "variant".equals(parts[0])) {
                    String variantId = parts[1];
                    boolean deleted = productDAO.deleteVariant(variantId);
                    result.put("success", deleted);
                    result.put("message", deleted ? "Variant deleted successfully" : "Failed to delete variant");
                } else if (parts.length == 1) {
                    String productId = parts[0];
                    boolean deleted = productDAO.deleteProduct(productId);
                    result.put("success", deleted);
                    result.put("message", deleted ? "Product deleted successfully" : "Failed to delete product");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    result.put("success", false);
                    result.put("message", "Invalid request");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product/variant", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error deleting product/variant");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }
}
