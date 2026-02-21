package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.WishListDAO;
import com.shopease.model.User;
import com.shopease.model.WishListItem;

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

//@WebServlet("/api/wishlist/*")
public class WishListServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(WishListServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private WishListDAO wishListDAO;

    @Override
    public void init() throws ServletException {
        wishListDAO = new WishListDAO();
    }

    // ================== GET wishlist items ==================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.put("success", false);
                result.put("message", "Login required");
                out.print(objectMapper.writeValueAsString(result));
                return;
            }

            User user = (User) session.getAttribute("user");
            String userId = user.getUserId();

            List<WishListItem> items = wishListDAO.getWishlistItemsByUserId(userId);
            result.put("success", true);
            result.put("userId", userId);
            result.put("wishlist", items);

            out.print(objectMapper.writeValueAsString(result));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching wishlist", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Server error while fetching wishlist");
            response.getWriter().print(objectMapper.writeValueAsString(result));
        }
    }

    // ================== ADD item to wishlist ==================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.put("success", false);
                result.put("message", "Login required");
                out.print(objectMapper.writeValueAsString(result));
                return;
            }

            User user = (User) session.getAttribute("user");
            String userId = user.getUserId();

            Map<String, Integer> data = objectMapper.readValue(request.getInputStream(), Map.class);
            Integer productId = data.get("productId");

            if (productId == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Product ID required");
                out.print(objectMapper.writeValueAsString(result));
                return;
            }

            boolean success = wishListDAO.addToWishlist(userId, productId);
            response.setStatus(success ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", success);
            result.put("message", success ? "Item added to wishlist" : "Failed to add item");
            out.print(objectMapper.writeValueAsString(result));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding to wishlist", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Server error while adding item");
            response.getWriter().print(objectMapper.writeValueAsString(result));
        }
    }

    // ================== REMOVE item from wishlist ==================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> result = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {
            String pathInfo = request.getPathInfo(); // e.g. /5

            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Item ID required");
                out.print(objectMapper.writeValueAsString(result));
                return;
            }

            int itemId;
            try {
                itemId = Integer.parseInt(pathInfo.substring(1));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("success", false);
                result.put("message", "Invalid item ID");
                out.print(objectMapper.writeValueAsString(result));
                return;
            }

            boolean success = wishListDAO.removeFromWishlist(itemId);
            response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            result.put("success", success);
            result.put("message", success ? "Item removed from wishlist" : "Failed to remove item");
            out.print(objectMapper.writeValueAsString(result));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error removing wishlist item", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Server error while removing item");
            response.getWriter().print(objectMapper.writeValueAsString(result));
        }
    }
}
