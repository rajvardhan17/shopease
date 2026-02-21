package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/api/session")
public class SessionServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    // ===== CORS helper =====
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
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        response.setHeader("Vary", "Origin");
    }

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
        Map<String, Object> result = new HashMap<>();

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            user.setPassword(null); // don't expose password
            result.put("success", true);
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "No active session");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }
}
