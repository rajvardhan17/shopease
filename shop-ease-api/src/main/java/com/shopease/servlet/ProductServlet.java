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

        String origin = req.getHeader("Origin");

        if (origin != null) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
        }

        resp.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
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

            String pathInfo = req.getPathInfo(); // /random OR /123
            String pageParam = req.getParameter("page");
            String sizeParam = req.getParameter("size");

            // ================= 1️⃣ RANDOM PRODUCTS =================
            if (pathInfo != null && pathInfo.equals("/random")) {

                List<Product> randomProducts = productDAO.getRandomProducts(10);

                result.put("success", true);
                result.put("products", randomProducts);
            }

            // ================= 2️⃣ SINGLE PRODUCT =================
            else if (pathInfo != null && pathInfo.length() > 1) {

                String productId = pathInfo.substring(1);
                Product product = productDAO.getProductById(productId);

                if (product != null) {
                    result.put("success", true);
                    result.put("product", product);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("success", false);
                    result.put("message", "Product not found");
                }
            }

            // ================= 3️⃣ PAGINATION =================
            else {

                int page = pageParam != null ? Integer.parseInt(pageParam) : 1;
                int size = sizeParam != null ? Integer.parseInt(sizeParam) : 10;

                List<Product> products = productDAO.getProducts(page, size);

                result.put("success", true);
                result.put("products", products);
                result.put("page", page);
                result.put("size", size);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching products", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("success", false);
            result.put("message", "Error fetching products");
        }

        mapper.writeValue(resp.getOutputStream(), result);
    }
}