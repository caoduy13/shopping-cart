package com.oem.ev.controller;

import com.oem.ev.model.User;
import com.oem.ev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller xử lý CRUD operations cho User
 * Cung cấp các endpoint để quản lý users
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Lấy tất cả users
     * @return List<User> danh sách tất cả users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Lấy user theo ID
     * @param id ID của user
     * @return User object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).get().username == authentication.name")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(createUserResponse(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy users theo role
     * @param role vai trò của user
     * @return List<User> danh sách users theo role
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVM_STAFF')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable User.UserRole role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Lấy users theo service center
     * @param serviceCenterId ID của service center
     * @return List<User> danh sách users thuộc service center
     */
    @GetMapping("/service-center/{serviceCenterId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVM_STAFF')")
    public ResponseEntity<List<User>> getUsersByServiceCenter(@PathVariable Long serviceCenterId) {
        List<User> users = userService.getUsersByServiceCenter(serviceCenterId);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Lấy users đang hoạt động
     * @return List<User> danh sách users đang hoạt động
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVM_STAFF')")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> users = userService.getActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Tạo user mới
     * @param user thông tin user mới
     * @return User đã được tạo
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createUserResponse(createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Tạo user thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật user
     * @param id ID của user cần cập nhật
     * @param userDetails thông tin user mới
     * @return User đã được cập nhật
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).get().username == authentication.name")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(createUserResponse(updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật user thất bại", e.getMessage()));
        }
    }
    
    /**
     * Xóa user (soft delete)
     * @param id ID của user cần xóa
     * @return thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(createSuccessResponse("Xóa user thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Xóa user thất bại", e.getMessage()));
        }
    }
    
    /**
     * Kích hoạt user
     * @param id ID của user cần kích hoạt
     * @return User đã được kích hoạt
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            User user = userService.activateUser(id);
            return ResponseEntity.ok(createUserResponse(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Kích hoạt user thất bại", e.getMessage()));
        }
    }
    
    /**
     * Thay đổi password
     * @param id ID của user
     * @param passwordRequest thông tin password mới
     * @return thông báo thay đổi password thành công
     */
    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).get().username == authentication.name")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody PasswordRequest passwordRequest) {
        try {
            userService.changePassword(id, passwordRequest.getNewPassword());
            return ResponseEntity.ok(createSuccessResponse("Thay đổi password thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Thay đổi password thất bại", e.getMessage()));
        }
    }
    
    /**
     * Tạo response object cho user (ẩn password)
     * @param user User object
     * @return Map chứa thông tin user
     */
    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> response = Map.of(
            "id", user.getId(),
            "username", user.getUsername(),
            "email", user.getEmail(),
            "fullName", user.getFullName(),
            "role", user.getRole(),
            "serviceCenterId", user.getServiceCenterId(),
            "isActive", user.getIsActive(),
            "createdAt", user.getCreatedAt(),
            "updatedAt", user.getUpdatedAt()
        );
        return response;
    }
    
    /**
     * Tạo error response
     * @param error thông báo lỗi
     * @param message chi tiết lỗi
     * @return Map chứa thông tin lỗi
     */
    private Map<String, String> createErrorResponse(String error, String message) {
        return Map.of("error", error, "message", message);
    }
    
    /**
     * Tạo success response
     * @param message thông báo thành công
     * @return Map chứa thông báo thành công
     */
    private Map<String, String> createSuccessResponse(String message) {
        return Map.of("message", message);
    }
    
    /**
     * Inner class cho password request
     */
    public static class PasswordRequest {
        private String newPassword;
        
        public PasswordRequest() {}
        
        public PasswordRequest(String newPassword) {
            this.newPassword = newPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}