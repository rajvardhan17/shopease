package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.ProductDAO;
import com.shopease.model.ProductVariant;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/admin/variants/*")
public class AdminVariantServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminVariantServlet.class.getName());
    private final ProductDAO productDAO = new ProductDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    // ===== Helper method for CORS headers =====
    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String[] allowedOrigins = {"http://localhost:3000", "http://localhost:8080"};
        String origin = request.getHeader("Origin");

        if (origin != null) {
            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equals(origin)) {
                    response.setHeader("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        response.setHeader("Vary", "Origin");
    }

    // ===== Handle preflight OPTIONS requests =====
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        setCorsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // ===== GET: Fetch variants by product ID =====
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        try {
            User admin = getAdminFromSession(req, resp);
            if (admin == null) return; // Already responded with UNAUTHORIZED

            String productId = extractIdFromPath(req, resp);
            if (productId == null) return; // Already responded with BAD_REQUEST

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", true,
                    "variants", productDAO.getVariantsByProductId(productId)
            ));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in GET /variants", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ===== POST: Add new variant =====
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        try {
            User admin = getAdminFromSession(req, resp);
            if (admin == null) return;

            ProductVariant variant = mapper.readValue(req.getInputStream(), ProductVariant.class);
            boolean success = productDAO.addVariant(variant);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", success,
                    "message", success ? "Variant added successfully" : "Failed to add variant"
            ));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in POST /variants", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ===== PUT: Update existing variant =====
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        try {
            User admin = getAdminFromSession(req, resp);
            if (admin == null) return;

            ProductVariant variant = mapper.readValue(req.getInputStream(), ProductVariant.class);
            boolean success = productDAO.updateVariant(variant);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", success,
                    "message", success ? "Variant updated successfully" : "Failed to update variant"
            ));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in PUT /variants", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ===== DELETE: Delete variant by ID =====
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        try {
            User admin = getAdminFromSession(req, resp);
            if (admin == null) return;

            String variantId = extractIdFromPath(req, resp);
            if (variantId == null) return;

            boolean success = productDAO.deleteVariant(variantId);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", success,
                    "message", success ? "Variant deleted successfully" : "Failed to delete variant"
            ));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in DELETE /variants", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ===== Helper: Extract admin user from session =====
    private User getAdminFromSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User admin = (session != null) ? (User) session.getAttribute("user") : null;

        if (admin == null || !"admin".equals(admin.getRole())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Admin login required"
            ));
            return null;
        }
        return admin;
    }

    // ===== Helper: Extract ID from URL path =====
    private String extractIdFromPath(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "ID is required in the URL"
            ));
            return null;
        }
        return pathInfo.substring(1);
    }
}
