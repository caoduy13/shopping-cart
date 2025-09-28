package com.oem.ev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Cấu hình Spring Security
 * Định nghĩa các quy tắc bảo mật, authentication và authorization
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    /**
     * Cấu hình SecurityFilterChain
     * @param http HttpSecurity object
     * @return SecurityFilterChain
     * @throws Exception nếu có lỗi cấu hình
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
            // Tắt CSRF vì sử dụng JWT
            .csrf(csrf -> csrf.disable())
            
            // Cấu hình CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Cấu hình session management - stateless vì dùng JWT
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Cấu hình authorization rules
            .authorizeHttpRequests(authz -> authz
                // Cho phép truy cập các endpoint công khai
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                
                // Yêu cầu authentication cho các endpoint khác
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/evm/**").hasAnyRole("EVM_STAFF", "ADMIN")
                .requestMatchers("/api/sc/**").hasAnyRole("SC_STAFF", "SC_TECHNICIAN", "ADMIN")
                
                // Yêu cầu authentication cho tất cả request khác
                .anyRequest().authenticated()
            )
            
            // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * Cấu hình CORS
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Cho phép các origin này truy cập
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Cho phép các HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Cho phép các headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Cho phép gửi credentials
        configuration.setAllowCredentials(true);
        
        // Cấu hình cho tất cả paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
    /**
     * Password encoder bean
     * Sử dụng BCrypt để mã hóa password
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Authentication manager bean
     * @param authConfig AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception nếu có lỗi cấu hình
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}