package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.UserDAO;
import com.shopease.model.User;
import com.shopease.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private final UserDAO userDAO = new UserDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String[] allowedOrigins = {
                "http://localhost:8080",
                "http://localhost:3000",
                "http://192.168.56.1:8080",
                "http://192.168.1.3:8080",
                "https://shopease-six-navy.vercel.app",
                "https://shopease-6p3wxf3cu-rajvardhan-singh-dewdas-projects.vercel.app"
        };

        String origin = request.getHeader("Origin");
        if (origin != null) {
            for (String allowed : allowedOrigins) {
                if (allowed.equals(origin)) {
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

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            String email = requestBody.get("email");
            String password = requestBody.get("password");
            boolean adminLogin = Boolean.parseBoolean(requestBody.getOrDefault("admin", "false")); // optional admin flag

            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Email and password are required");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            User user = userDAO.getUserByEmail(email);

            if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid email or password");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // ✅ Admin check
            if (adminLogin && !user.isAdmin()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "You are not authorized as admin");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // Successful login
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(30 * 60);
            user.setPassword(null);
            session.setAttribute("user", user);

            jsonResponse.put("success", true);
            jsonResponse.put("message", "Login successful");
            jsonResponse.put("user", user);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Server error: " + e.getMessage());
        }

        objectMapper.writeValue(response.getOutputStream(), jsonResponse);
    }
}