package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.OrderDAO;
import com.shopease.model.Order;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@WebServlet("/api/admin/orders/*")
public class AdminOrderServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);
        User admin = (User) (session != null ? session.getAttribute("user") : null);

        if (admin == null || !"admin".equals(admin.getRole())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getOutputStream(), Map.of("message", "Admin login required"));
            return;
        }

        List<Order> orders = orderDAO.getAllOrders(); // Make sure this method exists in OrderDAO
        mapper.writeValue(resp.getOutputStream(), orders);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);
        User admin = (User) (session != null ? session.getAttribute("user") : null);

        if (admin == null || !"admin".equals(admin.getRole())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getOutputStream(), Map.of("message", "Admin login required"));
            return;
        }

        Map<String, Object> data = mapper.readValue(req.getInputStream(), Map.class);
        int orderId = (Integer) data.get("orderId");
        String status = (String) data.get("status");

        boolean updated = orderDAO.updateOrderStatus(orderId, status);
        mapper.writeValue(resp.getOutputStream(), Map.of(
                "success", updated,
                "message", updated ? "Order status updated" : "Failed to update order"
        ));
    }
}
