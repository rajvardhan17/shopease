package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.UserDAO;
import com.shopease.model.User;
import com.shopease.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private final UserDAO userDAO = new UserDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ===== Helper method for CORS headers =====
    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        // List of allowed origins for development
        String[] allowedOrigins = {"http://localhost:8080", "http://localhost:3000", "http://192.168.56.1:8080", "http://192.168.1.3:8080"};

        String origin = request.getHeader("Origin");

        // Check if the request origin is in the allowed list
        if (origin != null) {
            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equals(origin)) {
                    response.setHeader("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }

        // If the origin wasn't explicitly allowed, you can leave it blocked or set a default (development only)
        if (response.getHeader("Access-Control-Allow-Origin") == null) {
            // Optional for development: set a default origin
            // response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        response.setHeader("Vary", "Origin"); // prevents caching issues
    }

    // ===== Handle preflight OPTIONS requests =====
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // ===== Handle login POST =====
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // Parse request body JSON
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            String email = requestBody.get("email");
            String password = requestBody.get("password");

            // Validate input
            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Email and password are required");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // Check if user exists
            User user = userDAO.getUserByEmail(email);
            if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid email or password");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // Login successful: create session
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            user.setPassword(null); // remove password before sending
            session.setAttribute("user", user);

            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Login successful");
            jsonResponse.put("user", user);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Server error: " + e.getMessage());
        }

        objectMapper.writeValue(response.getOutputStream(), jsonResponse);
    }
}
