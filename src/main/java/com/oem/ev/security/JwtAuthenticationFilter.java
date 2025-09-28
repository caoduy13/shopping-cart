package com.oem.ev.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * Filter này sẽ được gọi trước mỗi request để kiểm tra JWT token
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    /**
     * Method chính của filter - được gọi cho mỗi request
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain chuỗi các filter
     * @throws ServletException nếu có lỗi servlet
     * @throws IOException nếu có lỗi I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Lấy JWT token từ Authorization header
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;
        
        // Kiểm tra Authorization header có tồn tại và bắt đầu bằng "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract JWT token (bỏ "Bearer " prefix)
            jwt = authorizationHeader.substring(7);
            try {
                // Extract username từ JWT token
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Log error nếu không thể extract username
                logger.error("Cannot extract username from JWT token", e);
            }
        }
        
        // Nếu có username và chưa có authentication trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Load UserDetails từ database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            // Validate JWT token với UserDetails
            if (jwtUtil.validateToken(jwt, userDetails)) {
                
                // Tạo Authentication object
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,           // Principal (user details)
                        null,                 // Credentials (không cần vì đã có JWT)
                        userDetails.getAuthorities()  // Authorities (roles)
                    );
                
                // Set authentication details
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                // Set authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Tiếp tục với filter chain
        filterChain.doFilter(request, response);
    }
}