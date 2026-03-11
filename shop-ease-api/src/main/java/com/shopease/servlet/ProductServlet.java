package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.ProductDAO;
import com.shopease.model.Product;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/products/*")
public class ProductServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductServlet.class.getName());

    private final ProductDAO productDAO = new ProductDAO();
    private final ObjectMapper mapper = new ObjectMapper();

    // ================= CORS =================
    private void setCorsHeaders(HttpServletRequest req, HttpServletResponse resp) {
        String[] allowedOrigins = {
                "http://localhost:8080",
                "http://localhost:3000",
                "http://192.168.56.1:8080",
                "http://192.168.1.3:8080",
                "https://shopease-six-navy.vercel.app",
                "https://shopease-6p3wxf3cu-rajvardhan-singh-dewdas-projects.vercel.app"
        };
        String origin = req.getHeader("Origin");
        if (origin != null) {
            for (String allowedOrigin : allowedOrigins) {
                if (allowedOrigin.equals(origin)) {
                    resp.setHeader("Access-Control-Allow-Origin", origin);
                    break;
                }
            }
        }
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        resp.setHeader("Vary", "Origin");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setCorsHeaders(req, resp);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // ================= GET =================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        setCorsHeaders(req, resp);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {

            String pathInfo = req.getPathInfo(); // /random OR /{id}
            String pageParam = req.getParameter("page");
            String sizeParam = req.getParameter("size");

            // ================= 1️⃣ RANDOM PRODUCTS =================
            if ("/random".equals(pathInfo)) {
                List<Product> randomProducts = productDAO.getRandomProducts(10);
                result.put("success", true);
                result.put("products", randomProducts);
            }

            // ================= 2️⃣ SINGLE PRODUCT =================
            else if (pathInfo != null && pathInfo.length() > 1) {
                String productId = pathInfo.substring(1);

                // Use DAO method to fetch all products and filter by ID
                Product product = productDAO.getAllProducts(1, Integer.MAX_VALUE)
                        .stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElse(null);

                if (product != null) {
                    result.put("success", true);
                    result.put("product", product);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Product not found");
                }
            }

            // ================= 3️⃣ PAGINATED PRODUCTS =================
            else {
                int page = 1;
                int size = 10;

                try {
                    if (pageParam != null) page = Integer.parseInt(pageParam);
                    if (sizeParam != null) size = Integer.parseInt(sizeParam);
                } catch (NumberFormatException ignored) {}

                // Use your DAO's getAllProducts method
                List<Product> products = productDAO.getAllProducts(page, size);

                result.put("success", true);
                result.put("products", products);
                result.put("page", page);
                result.put("size", size);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching products", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Internal server error");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }
}