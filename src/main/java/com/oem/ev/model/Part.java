package com.oem.ev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class đại diện cho Part (Phụ tùng) trong hệ thống
 * Lưu trữ thông tin các bộ phận EV như pin, mô-tơ, BMS, inverter, bộ sạc...
 */
@Entity
@Table(name = "parts")
public class Part {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Part number không được để trống")
    @Column(name = "part_number", unique = true, nullable = false)
    private String partNumber;
    
    @NotBlank(message = "Part name không được để trống")
    @Column(name = "part_name", nullable = false)
    private String partName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartType partType;
    
    @Column(name = "serial_number", unique = true)
    private String serialNumber;
    
    @Column(name = "vehicle_vin")
    private String vehicleVin; // VIN của xe mà phụ tùng được lắp đặt
    
    @NotNull(message = "Warranty period không được null")
    @Column(name = "warranty_period_months")
    private Integer warrantyPeriodMonths; // Thời hạn bảo hành tính bằng tháng
    
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;
    
    @Column(name = "minimum_stock_level")
    private Integer minimumStockLevel = 0;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructor mặc định
    public Part() {}
    
    // Constructor với các tham số cơ bản
    public Part(String partNumber, String partName, PartType partType, Integer warrantyPeriodMonths) {
        this.partNumber = partNumber;
        this.partName = partName;
        this.partType = partType;
        this.warrantyPeriodMonths = warrantyPeriodMonths;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters và Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPartNumber() {
        return partNumber;
    }
    
    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }
    
    public String getPartName() {
        return partName;
    }
    
    public void setPartName(String partName) {
        this.partName = partName;
    }
    
    public PartType getPartType() {
        return partType;
    }
    
    public void setPartType(PartType partType) {
        this.partType = partType;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public String getVehicleVin() {
        return vehicleVin;
    }
    
    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }
    
    public Integer getWarrantyPeriodMonths() {
        return warrantyPeriodMonths;
    }
    
    public void setWarrantyPeriodMonths(Integer warrantyPeriodMonths) {
        this.warrantyPeriodMonths = warrantyPeriodMonths;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public Integer getMinimumStockLevel() {
        return minimumStockLevel;
    }
    
    public void setMinimumStockLevel(Integer minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Enum định nghĩa các loại phụ tùng EV
     */
    public enum PartType {
        BATTERY("Pin"),                    // Pin xe điện
        MOTOR("Mô-tơ"),                   // Mô-tơ điện
        BMS("BMS"),                       // Battery Management System
        INVERTER("Inverter"),             // Bộ biến tần
        CHARGER("Bộ sạc"),                // Bộ sạc
        CONTROLLER("Bộ điều khiển"),      // Bộ điều khiển
        DISPLAY("Màn hình"),              // Màn hình hiển thị
        SENSOR("Cảm biến"),               // Cảm biến
        CABLE("Cáp"),                     // Cáp kết nối
        OTHER("Khác");                    // Phụ tùng khác
        
        private final String displayName;
        
        PartType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}