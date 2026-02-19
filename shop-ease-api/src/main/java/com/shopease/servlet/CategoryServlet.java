package com.shopease.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.dao.CategoryDAO;
import com.shopease.model.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/categories/*")
public class CategoryServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CategoryServlet.class.getName());
    private CategoryDAO categoryDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
    }

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String pathInfo = request.getPathInfo(); // /1 or null
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Category> categories = categoryDAO.getAllCategories();
                out.print(objectMapper.writeValueAsString(categories));
            } else {
                int categoryId = Integer.parseInt(pathInfo.substring(1));
                Category category = categoryDAO.getCategoryById(categoryId);
                if (category != null) {
                    out.print(objectMapper.writeValueAsString(category));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Category not found\"}");
                }
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Invalid category ID\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"Server error\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || !"admin".equals(session.getAttribute("role"))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Admin login required\"}");
                return;
            }

            Category category = objectMapper.readValue(request.getInputStream(), Category.class);
            boolean success = categoryDAO.addCategory(category);
            response.setStatus(success ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":" + success + ",\"message\":\"" +
                    (success ? "Category added successfully" : "Failed to add category") + "\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding category", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Invalid request data\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || !"admin".equals(session.getAttribute("role"))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Admin login required\"}");
                return;
            }

            Category category = objectMapper.readValue(request.getInputStream(), Category.class);
            boolean success = categoryDAO.updateCategory(category);
            response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":" + success + ",\"message\":\"" +
                    (success ? "Category updated successfully" : "Failed to update category") + "\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating category", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Invalid request data\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(request, response);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession(false);
            if (session == null || !"admin".equals(session.getAttribute("role"))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"error\":\"Admin login required\"}");
                return;
            }

            String pathInfo = request.getPathInfo(); // /3
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Category ID required\"}");
                return;
            }

            int categoryId = Integer.parseInt(pathInfo.substring(1));
            boolean success = categoryDAO.deleteCategory(categoryId);
            response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"success\":" + success + ",\"message\":\"" +
                    (success ? "Category deleted" : "Failed to delete category") + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"error\":\"Invalid category ID\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting category", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"Server error\"}");
        }
    }
}
