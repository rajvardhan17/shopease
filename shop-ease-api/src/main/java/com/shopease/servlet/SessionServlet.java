package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/session")
public class SessionServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    // ===== CORS helper =====
    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {

        String[] allowedOrigins = {
                "http://localhost:3000",
                "http://localhost:8080",
                "https://shopease-six-navy.vercel.app",
                "https://shopease-6p3wxf3cu-rajvardhan-singh-dewdas-projects.vercel.app"
        };

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

    // ===== Handle Preflight =====
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        setCorsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // ===== Get Session User =====
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        Map<String, Object> result = new HashMap<>();

        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("user") != null) {

            User user = (User) session.getAttribute("user");
            user.setPassword(null); // hide password

            result.put("success", true);
            result.put("user", user);

        } else {

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            result.put("success", false);
            result.put("message", "No active session");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }

    // ===== Update Profile =====
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);
        resp.setContentType("application/json");

        Map<String, Object> result = new HashMap<>();

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {

            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            result.put("success", false);
            result.put("message", "User not logged in");

            mapper.writeValue(resp.getOutputStream(), result);
            return;
        }

        User sessionUser = (User) session.getAttribute("user");

        // Read updated data from frontend
        User updatedUser = mapper.readValue(req.getInputStream(), User.class);

        if (updatedUser.getFullName() != null)
            sessionUser.setFullName(updatedUser.getFullName());

        if (updatedUser.getEmail() != null)
            sessionUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPhone() != null)
            sessionUser.setPhone(updatedUser.getPhone());

        // Update session
        session.setAttribute("user", sessionUser);

        result.put("success", true);
        result.put("user", sessionUser);

        mapper.writeValue(resp.getOutputStream(), result);
    }
}