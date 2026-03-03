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

    // ================= CORS =================
    private void setCorsHeaders(HttpServletRequest req, HttpServletResponse resp) {

        String origin = req.getHeader("Origin");

        if (origin != null) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
        }

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
        return session != null &&
                session.getAttribute("user") != null &&
                "admin".equals(((User) session.getAttribute("user")).getRole());
    }

    // ================= GET (PUBLIC) =================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            String pathInfo = req.getPathInfo();

            // GET /api/products
            if (pathInfo == null || pathInfo.equals("/")) {

                List<Product> products = productDAO.getAllProducts();

                result.put("success", true);
                result.put("products", products);
            }

            // GET /api/products/{id}
            else {
                String productId = pathInfo.substring(1);

                Product product = productDAO.getProductById(productId);

                if (product == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Product not found");
                } else {
                    List<ProductVariant> variants =
                            productDAO.getVariantsByProductId(productId);

                    result.put("success", true);
                    result.put("product", product);
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

    // ================= POST (ADMIN ONLY) =================
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
            Product product = mapper.readValue(req.getInputStream(), Product.class);
            boolean added = productDAO.addProduct(product);

            result.put("success", added);
            result.put("message", added ? "Product added successfully" : "Failed to add product");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error adding product");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ================= PUT (ADMIN ONLY) =================
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
            Product product = mapper.readValue(req.getInputStream(), Product.class);
            boolean updated = productDAO.updateProduct(product);

            result.put("success", updated);
            result.put("message", updated ? "Product updated successfully" : "Failed to update product");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error updating product");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ================= DELETE (ADMIN ONLY) =================
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
                result.put("message", "Product ID required");
            } else {
                String productId = pathInfo.substring(1);
                boolean deleted = productDAO.deleteProduct(productId);

                result.put("success", deleted);
                result.put("message", deleted ? "Product deleted successfully" : "Failed to delete product");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error deleting product");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }
}