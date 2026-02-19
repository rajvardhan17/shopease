package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.UserDAO;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/admin/users/*")
public class AdminUserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
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

        List<User> users = userDAO.getAllUsers();
        mapper.writeValue(resp.getOutputStream(), users);
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

        User user = mapper.readValue(req.getInputStream(), User.class);
        boolean updated = userDAO.updateUser(user);
        mapper.writeValue(resp.getOutputStream(), Map.of(
                "success", updated,
                "message", updated ? "User updated" : "Failed to update user"
        ));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        HttpSession session = req.getSession(false);
        User admin = (User) (session != null ? session.getAttribute("user") : null);

        if (admin == null || !"admin".equals(admin.getRole())) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getOutputStream(), Map.of("message", "Admin login required"));
            return;
        }

        String pathInfo = req.getPathInfo(); // e.g. /{userId}
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getOutputStream(), Map.of(
                    "success", false,
                    "message", "User ID is required"
            ));
            return;
        }

        String userId = pathInfo.substring(1); // extract UUID as String
        boolean deleted = userDAO.deleteUser(userId);

        mapper.writeValue(resp.getOutputStream(), Map.of(
                "success", deleted,
                "message", deleted ? "User deleted" : "Failed to delete user"
        ));
    }
}
