package com.shopease.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class CorsFilter implements Filter {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:3000",
            "http://localhost:8080",
            "https://shopease-six-navy.vercel.app",
            "https://shopease-6p3wxf3cu-rajvardhan-singh-dewdas-projects.vercel.app"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");

        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
        }

        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization, X-Requested-With");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}