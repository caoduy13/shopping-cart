package com.oem.ev.controller;

import com.oem.ev.model.User;
import com.oem.ev.security.CustomUserDetailsService;
import com.oem.ev.security.JwtUtil;
import com.oem.ev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller xử lý authentication (login/logout)
 * Cung cấp các endpoint cho đăng nhập và đăng xuất
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    /**
     * Endpoint đăng nhập
     * @param loginRequest thông tin đăng nhập (username, password)
     * @return JWT token và thông tin user
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Tạo Authentication object
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),    // Username
                    loginRequest.getPassword()     // Password
                )
            );
            
            // Lấy UserDetails từ authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Tạo JWT token
            String token = jwtUtil.generateToken(userDetails);
            
            // Lấy thông tin user từ database
            User user = userService.getUserByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            
            // Tạo response object
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("user", createUserResponse(user));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // Tạo error response
            Map<String, String> error = new HashMap<>();
            error.put("error", "Đăng nhập thất bại");
            error.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Endpoint đăng xuất
     * @return thông báo đăng xuất thành công
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Với JWT, logout chỉ cần xóa token ở client side
        // Server không cần làm gì đặc biệt
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint đăng ký user mới (chỉ dành cho Admin)
     * @param user thông tin user mới
     * @return User đã được tạo
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createUserResponse(createdUser));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Đăng ký thất bại");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Endpoint lấy thông tin user hiện tại
     * @param authentication thông tin authentication hiện tại
     * @return thông tin user
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
            
            return ResponseEntity.ok(createUserResponse(user));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Không thể lấy thông tin user");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Tạo response object cho user (ẩn password)
     * @param user User object
     * @return Map chứa thông tin user
     */
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("username", user.getUsername());
        userResponse.put("email", user.getEmail());
        userResponse.put("fullName", user.getFullName());
        userResponse.put("role", user.getRole());
        userResponse.put("serviceCenterId", user.getServiceCenterId());
        userResponse.put("isActive", user.getIsActive());
        userResponse.put("createdAt", user.getCreatedAt());
        userResponse.put("updatedAt", user.getUpdatedAt());
        
        return userResponse;
    }
    
    /**
     * Inner class cho login request
     */
    public static class LoginRequest {
        private String username;
        private String password;
        
        // Constructors
        public LoginRequest() {}
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
        
        // Getters và Setters
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}