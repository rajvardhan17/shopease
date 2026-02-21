package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.CartDAO;
import com.shopease.model.Cart;
import com.shopease.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//@WebServlet("/api/cart/*")
public class CartServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CartServlet.class.getName());
    private CartDAO cartDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        cartDAO = new CartDAO();
    }

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"User not logged in\"}");
                return;
            }

            String pathInfo = request.getPathInfo(); // /1
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"User ID required in path\"}");
                return;
            }

            int userId;
            try {
                userId = Integer.parseInt(pathInfo.substring(1));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid user ID\"}");
                return;
            }

            Cart cart = cartDAO.getCartByUserId(String.valueOf(userId));
            if (cart != null) {
                List<CartItem> items = cartDAO.getCartItems(cart.getCartId());
                Map<String, Object> result = new HashMap<>();
                result.put("cart", cart);
                result.put("items", items);
                out.print(objectMapper.writeValueAsString(result));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Cart not found for this user\"}");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching cart", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"User not logged in\"}");
                return;
            }

            String path = request.getPathInfo();
            if (path == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Missing endpoint path\"}");
                return;
            }

            switch (path) {
                case "/add": {
                    CartItem item = objectMapper.readValue(request.getInputStream(), CartItem.class);
                    String userId = (String) session.getAttribute("userId");
                    Cart cart = cartDAO.getCartByUserId(userId);
                    item.setCartId(cart.getCartId());

                    boolean success = cartDAO.addItemToCart(item);
                    response.setStatus(success ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\":" + success + ",\"message\":\"" +
                            (success ? "Item added to cart" : "Failed to add item") + "\"}");
                    break;
                }
                case "/update": {
                    Map<String, Object> data = objectMapper.readValue(request.getInputStream(), Map.class);
                    String cartItemId = ((String) data.get("cartItemId"));
                    int quantity = ((Number) data.get("quantity")).intValue();

                    boolean success = cartDAO.updateQuantity(cartItemId, quantity);
                    response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"success\":" + success + ",\"message\":\"" +
                            (success ? "Quantity updated" : "Failed to update quantity") + "\"}");
                    break;
                }
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Invalid endpoint\"}");
                    break;
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing cart POST", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"User not logged in\"}");
                return;
            }

            String pathInfo = request.getPathInfo(); // /remove/5
            if (pathInfo == null || !pathInfo.startsWith("/remove/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid path. Use /remove/{cartItemId}\"}");
                return;
            }

            String cartItemId = pathInfo.substring("/remove/".length());
            boolean success = cartDAO.removeItem(cartItemId);
            response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":" + success + ",\"message\":\"" +
                    (success ? "Item removed" : "Failed to remove item") + "\"}");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing cart item", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
