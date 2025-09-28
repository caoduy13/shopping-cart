package com.oem.ev.repository;

import com.oem.ev.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho User entity
 * JpaRepository cung cấp các method CRUD cơ bản và custom query methods
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Tìm user theo username
     * @param username tên đăng nhập
     * @return Optional<User> - có thể null nếu không tìm thấy
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Tìm user theo email
     * @param email địa chỉ email
     * @return Optional<User> - có thể null nếu không tìm thấy
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Kiểm tra username đã tồn tại chưa
     * @param username tên đăng nhập
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByUsername(String username);
    
    /**
     * Kiểm tra email đã tồn tại chưa
     * @param email địa chỉ email
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByEmail(String email);
    
    /**
     * Tìm tất cả user theo role
     * @param role vai trò của user
     * @return List<User> danh sách user có role tương ứng
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * Tìm tất cả user theo service center
     * @param serviceCenterId ID của service center
     * @return List<User> danh sách user thuộc service center
     */
    List<User> findByServiceCenterId(Long serviceCenterId);
    
    /**
     * Tìm tất cả user đang hoạt động
     * @param isActive trạng thái hoạt động
     * @return List<User> danh sách user đang hoạt động
     */
    List<User> findByIsActive(Boolean isActive);
    
    /**
     * Tìm user theo role và service center
     * @param role vai trò của user
     * @param serviceCenterId ID của service center
     * @return List<User> danh sách user thỏa mãn điều kiện
     */
    List<User> findByRoleAndServiceCenterId(User.UserRole role, Long serviceCenterId);
    
    /**
     * Custom query để tìm user theo username hoặc email
     * @param username tên đăng nhập
     * @param email địa chỉ email
     * @return Optional<User> user tìm được
     */
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
    
    /**
     * Custom query để tìm user theo role và trạng thái hoạt động
     * @param role vai trò của user
     * @param isActive trạng thái hoạt động
     * @return List<User> danh sách user thỏa mãn điều kiện
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isActive = :isActive")
    List<User> findByRoleAndIsActive(@Param("role") User.UserRole role, @Param("isActive") Boolean isActive);
}