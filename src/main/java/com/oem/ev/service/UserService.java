package com.oem.ev.service;

import com.oem.ev.model.User;
import com.oem.ev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class cho User management
 * Cung cấp các business logic cho CRUD operations của User
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Tạo user mới
     * @param user User object cần tạo
     * @return User đã được tạo
     * @throws IllegalArgumentException nếu username hoặc email đã tồn tại
     */
    public User createUser(User user) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + user.getUsername());
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
        }
        
        // Mã hóa password trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set timestamps
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // Lưu user vào database
        return userRepository.save(user);
    }
    
    /**
     * Lấy user theo ID
     * @param id ID của user
     * @return Optional<User> - có thể null nếu không tìm thấy
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Lấy user theo username
     * @param username tên đăng nhập
     * @return Optional<User> - có thể null nếu không tìm thấy
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Lấy user theo email
     * @param email địa chỉ email
     * @return Optional<User> - có thể null nếu không tìm thấy
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Lấy tất cả users
     * @return List<User> danh sách tất cả users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Lấy users theo role
     * @param role vai trò của user
     * @return List<User> danh sách users theo role
     */
    public List<User> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Lấy users theo service center
     * @param serviceCenterId ID của service center
     * @return List<User> danh sách users thuộc service center
     */
    public List<User> getUsersByServiceCenter(Long serviceCenterId) {
        return userRepository.findByServiceCenterId(serviceCenterId);
    }
    
    /**
     * Lấy users đang hoạt động
     * @return List<User> danh sách users đang hoạt động
     */
    public List<User> getActiveUsers() {
        return userRepository.findByIsActive(true);
    }
    
    /**
     * Cập nhật user
     * @param id ID của user cần cập nhật
     * @param userDetails thông tin user mới
     * @return User đã được cập nhật
     * @throws IllegalArgumentException nếu user không tồn tại
     */
    public User updateUser(Long id, User userDetails) {
        // Tìm user hiện tại
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại với ID: " + id));
        
        // Kiểm tra username có thay đổi không và đã tồn tại chưa
        if (!existingUser.getUsername().equals(userDetails.getUsername()) && 
            userRepository.existsByUsername(userDetails.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + userDetails.getUsername());
        }
        
        // Kiểm tra email có thay đổi không và đã tồn tại chưa
        if (!existingUser.getEmail().equals(userDetails.getEmail()) && 
            userRepository.existsByEmail(userDetails.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + userDetails.getEmail());
        }
        
        // Cập nhật thông tin user
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setFullName(userDetails.getFullName());
        existingUser.setRole(userDetails.getRole());
        existingUser.setServiceCenterId(userDetails.getServiceCenterId());
        existingUser.setIsActive(userDetails.getIsActive());
        
        // Cập nhật password nếu có
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        // Cập nhật timestamp
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        // Lưu user đã cập nhật
        return userRepository.save(existingUser);
    }
    
    /**
     * Xóa user (soft delete)
     * @param id ID của user cần xóa
     * @throws IllegalArgumentException nếu user không tồn tại
     */
    public void deleteUser(Long id) {
        // Tìm user hiện tại
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại với ID: " + id));
        
        // Soft delete - chỉ set isActive = false
        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        
        // Lưu thay đổi
        userRepository.save(user);
    }
    
    /**
     * Kích hoạt lại user
     * @param id ID của user cần kích hoạt
     * @return User đã được kích hoạt
     * @throws IllegalArgumentException nếu user không tồn tại
     */
    public User activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại với ID: " + id));
        
        user.setIsActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    /**
     * Thay đổi password của user
     * @param id ID của user
     * @param newPassword password mới
     * @return User đã được cập nhật password
     * @throws IllegalArgumentException nếu user không tồn tại
     */
    public User changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại với ID: " + id));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
}