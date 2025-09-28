package com.oem.ev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class của ứng dụng Spring Boot
 * @SpringBootApplication annotation bao gồm:
 * - @Configuration: Đánh dấu class này là configuration class
 * - @EnableAutoConfiguration: Tự động cấu hình Spring Boot
 * - @ComponentScan: Tự động scan các component trong package này và sub-packages
 */
@SpringBootApplication
public class EvWarrantyManagementApplication {

    /**
     * Main method - entry point của ứng dụng
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Khởi chạy Spring Boot application
        SpringApplication.run(EvWarrantyManagementApplication.class, args);
    }
}