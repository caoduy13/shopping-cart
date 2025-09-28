package com.oem.ev.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class để xử lý JWT tokens
 * Cung cấp các method để tạo, validate và extract thông tin từ JWT
 */
@Component
public class JwtUtil {
    
    // JWT secret key từ application.yml
    @Value("${spring.security.jwt.secret}")
    private String secret;
    
    // JWT expiration time từ application.yml (milliseconds)
    @Value("${spring.security.jwt.expiration}")
    private Long expiration;
    
    /**
     * Tạo JWT token từ UserDetails
     * @param userDetails thông tin user
     * @return JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Tạo JWT token với custom claims
     * @param claims thông tin bổ sung
     * @param subject username
     * @return JWT token string
     */
    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)                    // Set custom claims
                .setSubject(subject)                  // Set username làm subject
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Thời gian tạo token
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Thời gian hết hạn
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Ký token với secret key
                .compact();                           // Tạo token string
    }
    
    /**
     * Extract username từ JWT token
     * @param token JWT token
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Extract expiration date từ JWT token
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Extract custom claim từ JWT token
     * @param token JWT token
     * @param claimsResolver function để extract claim
     * @return claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Extract tất cả claims từ JWT token
     * @param token JWT token
     * @return Claims object
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())       // Set secret key để verify
                .build()
                .parseClaimsJws(token)               // Parse token
                .getBody();                          // Get claims
    }
    
    /**
     * Kiểm tra token có hết hạn không
     * @param token JWT token
     * @return true nếu chưa hết hạn, false nếu đã hết hạn
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Validate JWT token với UserDetails
     * @param token JWT token
     * @param userDetails thông tin user
     * @return true nếu token hợp lệ, false nếu không
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    /**
     * Validate JWT token (chỉ kiểm tra format và expiration)
     * @param token JWT token
     * @return true nếu token hợp lệ, false nếu không
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Tạo SecretKey từ secret string
     * @return SecretKey object
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}