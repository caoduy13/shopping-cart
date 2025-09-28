package com.oem.ev.security;

import com.oem.ev.model.User;
import com.oem.ev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetailsService để load user từ database
 * Implement UserDetailsService interface của Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Load user từ database theo username
     * @param username tên đăng nhập
     * @return UserDetails object
     * @throws UsernameNotFoundException nếu không tìm thấy user
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // Tìm user trong database theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        // Kiểm tra user có đang hoạt động không
        if (!user.getIsActive()) {
            throw new UsernameNotFoundException("User is inactive: " + username);
        }
        
        // Tạo và trả về UserDetails object
        return new CustomUserPrincipal(user);
    }
    
    /**
     * Custom UserDetails implementation
     * Wrapper cho User entity để implement UserDetails interface
     */
    public static class CustomUserPrincipal implements UserDetails {
        
        private final User user;
        
        public CustomUserPrincipal(User user) {
            this.user = user;
        }
        
        /**
         * Get authorities (roles) của user
         * @return Collection of GrantedAuthority
         */
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // Tạo SimpleGrantedAuthority từ user role
            return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );
        }
        
        /**
         * Get password của user
         * @return password string
         */
        @Override
        public String getPassword() {
            return user.getPassword();
        }
        
        /**
         * Get username của user
         * @return username string
         */
        @Override
        public String getUsername() {
            return user.getUsername();
        }
        
        /**
         * Kiểm tra account có hết hạn không
         * @return true nếu account chưa hết hạn
         */
        @Override
        public boolean isAccountNonExpired() {
            return true; // Không có logic hết hạn account trong hệ thống này
        }
        
        /**
         * Kiểm tra account có bị khóa không
         * @return true nếu account chưa bị khóa
         */
        @Override
        public boolean isAccountNonLocked() {
            return true; // Không có logic khóa account trong hệ thống này
        }
        
        /**
         * Kiểm tra credentials có hết hạn không
         * @return true nếu credentials chưa hết hạn
         */
        @Override
        public boolean isCredentialsNonExpired() {
            return true; // Không có logic hết hạn credentials trong hệ thống này
        }
        
        /**
         * Kiểm tra account có được enable không
         * @return true nếu account được enable
         */
        @Override
        public boolean isEnabled() {
            return user.getIsActive();
        }
        
        /**
         * Get User entity
         * @return User object
         */
        public User getUser() {
            return user;
        }
    }
}