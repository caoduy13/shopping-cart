package com.oem.ev;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration test cho main application
 * Kiểm tra Spring Boot context có load thành công không
 */
@SpringBootTest
@ActiveProfiles("test")
class EvWarrantyManagementApplicationTests {
    
    /**
     * Test Spring Boot application context loads successfully
     * Kiểm tra ứng dụng Spring Boot có khởi động thành công không
     */
    @Test
    void contextLoads() {
        // Test sẽ pass nếu Spring Boot context load thành công
        // Nếu có lỗi cấu hình, test này sẽ fail
    }
}