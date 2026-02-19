package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shopease.dao.AddressDAO;
import com.shopease.model.Address;
import com.shopease.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/addresses/*")
public class AddressServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AddressServlet.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AddressDAO addressDAO = new AddressDAO();

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
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // ===== GET all addresses for logged-in user =====
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> jsonResponse = new HashMap<>();
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Login required");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            String userId = ((User) session.getAttribute("user")).getUserId();
            List<Address> addresses = addressDAO.getAddressesByUserId(userId);

            objectMapper.writeValue(response.getOutputStream(), addresses);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching addresses", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Server error");
            objectMapper.writeValue(response.getOutputStream(), jsonResponse);
        }
    }

    // ===== POST new address =====
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> jsonResponse = new HashMap<>();

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Login required");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            String userId = ((User) session.getAttribute("user")).getUserId();
            Address address = objectMapper.readValue(request.getInputStream(), Address.class);
            address.setUserId(userId);

            boolean success = addressDAO.addAddress(address);
            jsonResponse.put("success", success);
            jsonResponse.put("message", success ? "Address added" : "Failed to add address");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding address", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Server error");
        }

        objectMapper.writeValue(response.getOutputStream(), jsonResponse);
    }

    // ===== PUT update address =====
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> jsonResponse = new HashMap<>();

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Login required");
                objectMapper.writeValue(response.getOutputStream(), jsonResponse);
                return;
            }

            Address address = objectMapper.readValue(request.getInputStream(), Address.class);
            String userId = ((User) session.getAttribute("user")).getUserId();
            address.setUserId(userId);

            boolean success = addressDAO.updateAddress(address);
            jsonResponse.put("success", success);
            jsonResponse.put("message", success ? "Address updated" : "Failed to update address");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating address", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Server error");
        }

        objectMapper.writeValue(response.getOutputStream(), jsonResponse);
    }

    // ===== DELETE address =====
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectNode resNode = objectMapper.createObjectNode();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            resNode.put("success", false);
            resNode.put("message", "Login required");
            response.getWriter().write(resNode.toString());
            return;
        }

        String userId = ((User) session.getAttribute("user")).getUserId();
        List<Address> addresses = addressDAO.getAddressesByUserId(userId);

        if (addresses.isEmpty()) {
            resNode.put("success", false);
            resNode.put("message", "No address found for user");
            response.getWriter().write(resNode.toString());
            return;
        }

        String addressId = addresses.get(0).getAddressId(); // first address
        boolean deleted = addressDAO.deleteAddress(addressId);

        resNode.put("success", deleted);
        resNode.put("message", deleted ? "Address deleted successfully" : "Failed to delete address");

        response.getWriter().write(resNode.toString());
    }

}
