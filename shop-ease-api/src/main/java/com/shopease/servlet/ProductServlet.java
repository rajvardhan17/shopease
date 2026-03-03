package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.ProductDAO;
import com.shopease.model.Product;
import com.shopease.model.User;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        resp.setHeader("Access-Control-Allow-Credentials", "true");
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

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {

            String pathInfo = req.getPathInfo();   // /random OR /123
            String category = req.getParameter("category");

            List<Product> products = productDAO.getAllProducts();

            // ---- 1️⃣ GET 10 RANDOM PRODUCTS ----
            if (pathInfo != null && pathInfo.equals("/random")) {

                Collections.shuffle(products);
                List<Product> randomProducts = products.stream()
                        .limit(10)
                        .collect(Collectors.toList());

                result.put("success", true);
                result.put("products", randomProducts);

            }

            // ---- 2️⃣ GET SINGLE PRODUCT BY ID ----
            else if (pathInfo != null && pathInfo.length() > 1) {

                String productId = pathInfo.substring(1);
                Product product = productDAO.getProductById(productId);

                if (product != null) {
                    result.put("success", true);
                    result.put("product", product);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Product not found");
                }
            }

            // ---- 3️⃣ FILTER BY CATEGORY ----
            else if (category != null) {

                List<Product> filtered = products.stream()
                        .filter(p -> category.equalsIgnoreCase(p.getCategoryId()))
                        .collect(Collectors.toList());

                result.put("success", true);
                result.put("products", filtered);
            }

            // ---- 4️⃣ RETURN ALL PRODUCTS ----
            else {
                result.put("success", true);
                result.put("products", products);
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