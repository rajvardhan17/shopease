package com.shopease.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.OrderDAO;
import com.shopease.model.Order;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/payment")
public class PaymentServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(PaymentServlet.class.getName());
    private final ObjectMapper mapper = new ObjectMapper();
    private final OrderDAO orderDAO = new OrderDAO();

    // ===== CORS helper =====
    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if ("http://localhost:3000".equals(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        response.setHeader("Vary", "Origin");
    }

    // ===== Handle preflight =====
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\": false, \"message\": \"Login required to make payment\"}");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            Map<String, String> paymentData = mapper.readValue(
                    request.getInputStream(),
                    new TypeReference<Map<String, String>>() {}
            );

            String orderIdStr = paymentData.get("orderId");
            String paymentMethod = paymentData.get("paymentMethod");
            String details = paymentData.get("details");

            if (orderIdStr == null || paymentMethod == null || details == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Invalid payment data\"}");
                return;
            }

            int orderId;
            try {
                orderId = Integer.parseInt(orderIdStr);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Invalid order ID\"}");
                return;
            }

            Order order = orderDAO.getOrderById(orderId);
            if (order == null || !user.getUserId().equals(order.getUserId())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print("{\"success\": false, \"message\": \"Order not found or does not belong to user\"}");
                return;
            }

            boolean paymentSuccess = processPayment(user.getUserId(), orderId, paymentMethod, details);

            if (paymentSuccess) {
                out.print("{\"success\": true, \"message\": \"Payment successful\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"success\": false, \"message\": \"Payment failed\"}");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Payment processing error", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Server error during payment\"}");
        }
    }

    private boolean processPayment(String userId, int orderId, String method, String details) {
        LOGGER.info("Processing payment for UserID: " + userId +
                ", OrderID: " + orderId +
                ", Method: " + method +
                ", Details: " + details);

        boolean paymentSuccess = true; // Simulate success

        if (paymentSuccess) {
            boolean updated = orderDAO.updateOrderStatus(orderId, "PAID");
            if (!updated) {
                LOGGER.warning("Failed to update order status for orderId: " + orderId);
                paymentSuccess = false;
            }
        }

        return paymentSuccess;
    }
}
