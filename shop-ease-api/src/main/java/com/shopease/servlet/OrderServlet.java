package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.OrderDAO;
import com.shopease.model.Order;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"success\": false, \"message\": \"Login required\"}");
                return;
            }

            User user = (User) session.getAttribute("user");
            String userId = user.getUserId(); // UUID as String

            // Optional: fetch single order by ID
            String pathInfo = req.getPathInfo(); // e.g., /5
            if (pathInfo != null && !pathInfo.equals("/")) {
                try {
                    int orderId = Integer.parseInt(pathInfo.substring(1));
                    Order order = orderDAO.getOrderById(orderId);

                    if (order != null && userId.equals(order.getUserId())) {
                        out.print(mapper.writeValueAsString(order));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"success\": false, \"message\": \"Order not found\"}");
                    }
                    return;
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\": false, \"message\": \"Invalid order ID\"}");
                    return;
                }
            }

            // Fetch all orders for user
            List<Order> orders = orderDAO.getOrdersByUserId(userId);
            out.print(mapper.writeValueAsString(orders));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching orders", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print("{\"success\": false, \"message\": \"Server error\"}");
        }
    }
}
