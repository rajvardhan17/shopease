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

@WebServlet("/api/user/signup")
public class RegisterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    private final UserDAO userDAO = new UserDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ===== Helper method for CORS headers =====
    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        // List of allowed origins for development
        String[] allowedOrigins = {"http://localhost:8080", "http://localhost:3000", "http://192.168.56.1:8080", "http://192.168.1.3:8080"};

        String origin = request.getHeader("Origin");

        if (origin != null) {
            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equals(origin)) {
                    response.setHeader("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }

        // Optional: set a default for development if no match
        if (response.getHeader("Access-Control-Allow-Origin") == null) {
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

    // ===== Handle registration POST =====
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();

        try {
            // Read JSON input
            Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
            String fullName = requestBody.get("fullName");
            String email = requestBody.get("email");
            String password = requestBody.get("password");
            String phone = requestBody.get("phone");

            // Validate required fields
            if (fullName == null || email == null || password == null ||
                    fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "fullName, email, and password are required");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // Validate email format
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid email format");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // Password length check
            if (password.length() < 6) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Password must be at least 6 characters");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // Check if email already exists
            if (userDAO.getUserByEmail(email) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Email already registered");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            // Hash password
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Create User object
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(hashedPassword);
            user.setPhone(phone);
            user.setRole("user");

            boolean success = userDAO.registerUser(user);

            if (success) {
                // Create session
                HttpSession session = request.getSession(true);
                user.setPassword(null); // hide password
                session.setAttribute("user", user);

                response.setStatus(HttpServletResponse.SC_CREATED);
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Registration successful");
                jsonResponse.put("user", user);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Failed to register user");
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during registration", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Server error: " + e.getMessage());
        }

        objectMapper.writeValue(response.getOutputStream(), jsonResponse);
    }
}

