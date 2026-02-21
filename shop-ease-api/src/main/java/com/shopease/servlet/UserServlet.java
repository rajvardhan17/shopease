package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.UserDAO;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> jsonResponse = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {
            String path = request.getPathInfo(); // /signup or /login

            if (path == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid endpoint");
                out.print(objectMapper.writeValueAsString(jsonResponse));
                return;
            }

            switch (path) {
                case "/signup" -> handleSignup(request, response, jsonResponse, out);
                case "/login" -> handleLogin(request, response, jsonResponse, out);
                default -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Endpoint not found");
                    out.print(objectMapper.writeValueAsString(jsonResponse));
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in UserServlet", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Server error");
            response.getWriter().print(objectMapper.writeValueAsString(jsonResponse));
        }
    }

    private void handleSignup(HttpServletRequest request, HttpServletResponse response,
                              Map<String, Object> jsonResponse, PrintWriter out) throws IOException {

        User user = objectMapper.readValue(request.getInputStream(), User.class);

        // Check if email already exists
        boolean exists = userDAO.getUserByEmail(user.getEmail()) != null;
        if (exists) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Email already registered");
        } else {
            boolean success = userDAO.registerUser(user);
            jsonResponse.put("success", success);
            jsonResponse.put("message", success ? "Signup successful" : "Signup failed");
            response.setStatus(success ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
        }

        out.print(objectMapper.writeValueAsString(jsonResponse));
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response,
                             Map<String, Object> jsonResponse, PrintWriter out) throws IOException {

        Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);
        String email = loginData.get("email");
        String password = loginData.get("password");

        if (email == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Email and password are required");
            out.print(objectMapper.writeValueAsString(jsonResponse));
            return;
        }

        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            jsonResponse.put("success", true);
            jsonResponse.put("message", "Login successful");
            jsonResponse.put("user", user);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid credentials");
        }

        out.print(objectMapper.writeValueAsString(jsonResponse));
    }
}
