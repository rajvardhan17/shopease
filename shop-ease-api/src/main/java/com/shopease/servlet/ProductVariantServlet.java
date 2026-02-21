package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.ProductDAO;
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

//@WebServlet("/api/variants/*")
public class ProductVariantServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductVariantServlet.class.getName());
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

    // ===== GET variants by product ID =====
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            String pathInfo = req.getPathInfo(); // expected: /product/{productId}
            if (pathInfo == null || !pathInfo.startsWith("/product/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Invalid path. Use /product/{productId}");
            } else {
                String productId = pathInfo.substring("/product/".length());
                List<ProductVariant> variants = productDAO.getVariantsByProductId(productId);
                result.put("success", true);
                result.put("variants", variants);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching variants", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Server error");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ===== POST: add variant =====
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
            ProductVariant variant = mapper.readValue(req.getInputStream(), ProductVariant.class);
            boolean success = productDAO.addVariant(variant);
            resp.setStatus(success ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", success);
            result.put("message", success ? "Variant added successfully" : "Failed to add variant");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding variant", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Server error");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ===== PUT: update variant =====
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
            ProductVariant variant = mapper.readValue(req.getInputStream(), ProductVariant.class);
            boolean success = productDAO.updateVariant(variant);
            resp.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", success);
            result.put("message", success ? "Variant updated successfully" : "Failed to update variant");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating variant", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Server error");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ===== DELETE: remove variant =====
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
            String pathInfo = req.getPathInfo(); // expected: /{variantId}
            if (pathInfo == null || pathInfo.length() <= 1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Variant ID required");
            } else {
                String variantId = pathInfo.substring(1);
                boolean success = productDAO.deleteVariant(variantId);
                resp.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", success);
                result.put("message", success ? "Variant deleted successfully" : "Failed to delete variant");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting variant", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Server error");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }
}
