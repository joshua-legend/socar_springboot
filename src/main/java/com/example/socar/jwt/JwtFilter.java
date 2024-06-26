package com.example.socar.jwt;

import com.example.socar.ApiResponse;
import com.example.socar.admin.AdminDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;

    private AdminDetailsService adminDetailsService;


    public JwtFilter(JwtUtil jwtUtil, AdminDetailsService adminDetailsService) {
        this.jwtUtil = jwtUtil;
        this.adminDetailsService = adminDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, "Unauthorized: Bearer token is missing or malformed");
            return;
        }

        // "Bearer " 뒤에 토큰이 없는 경우
        String token = authorizationHeader.substring(7);
        if (token == null || token.trim().isEmpty() || "undefined".equals(token)) {
            sendErrorResponse(response, "Unauthorized: Token is null, empty or undefined");
            return;
        }

        String username = jwtUtil.extractUsername(token);
        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            sendErrorResponse(response, "Unauthorized: Invalid token or user already authenticated");
            return;
        }

        if (!jwtUtil.validateToken(token, username)) {
            sendErrorResponse(response, "Unauthorized: Invalid token");
            return;
        }

        var userDetails = adminDetailsService.loadUserByUsername(username);
        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(request, response);
    }
    @Autowired
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ApiResponse<String> errorResponse = new ApiResponse<>("ERROR", HttpServletResponse.SC_UNAUTHORIZED, 0, message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}