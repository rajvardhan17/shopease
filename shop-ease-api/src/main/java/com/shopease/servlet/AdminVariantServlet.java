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

    // ================= CORS =================
    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {

        String[] allowedOrigins = {
                "http://localhost:3000",
                "http://localhost:8080",
                "https://shopease-6p3wxf3cu-rajvardhan-singh-dewdas-projects.vercel.app"
        };

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
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Vary", "Origin");
    }

    // ================= OPTIONS =================
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        setCorsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // ================= GET (Admin Only) =================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {

            User admin = getAdminFromSession(req, resp);
            if (admin == null) return;

            String productId = extractIdFromPath(req, resp);
            if (productId == null) return;

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", true,
                    "variants", productDAO.getVariantsByProductId(productId)
            ));

        } catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Error fetching variants", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ================= POST (Add Variant) =================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {

            User admin = getAdminFromSession(req, resp);
            if (admin == null) return;

            ProductVariant variant = mapper.readValue(req.getInputStream(), ProductVariant.class);

            boolean success = productDAO.addVariant(variant);

            resp.setStatus(success ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", success,
                    "message", success ? "Variant added successfully" : "Failed to add variant"
            ));

        } catch (Exception e) {

            LOGGER.log(Level.SEVERE, "Error adding variant", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ================= PUT (Update Variant) =================
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

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

            LOGGER.log(Level.SEVERE, "Error updating variant", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ================= DELETE =================
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

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

            LOGGER.log(Level.SEVERE, "Error deleting variant", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Internal server error"
            ));
        }
    }

    // ================= SESSION ADMIN CHECK =================
    private User getAdminFromSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession(false);

        User admin = (session != null) ? (User) session.getAttribute("user") : null;

        if (admin == null || !"admin".equalsIgnoreCase(admin.getRole())) {

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "Admin authentication required"
            ));

            return null;
        }

        return admin;
    }

    // ================= PATH HELPER =================
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