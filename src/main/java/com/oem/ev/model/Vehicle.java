package com.oem.ev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class đại diện cho Vehicle (Xe) trong hệ thống
 * Lưu trữ thông tin xe điện và các phụ tùng được lắp đặt
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "VIN không được để trống")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN phải có đúng 17 ký tự")
    @Column(unique = true, nullable = false, length = 17)
    private String vin; // Vehicle Identification Number
    
    @NotBlank(message = "Model không được để trống")
    @Column(nullable = false)
    private String model;
    
    @NotBlank(message = "Brand không được để trống")
    @Column(nullable = false)
    private String brand;
    
    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;
    
    @Column(name = "warranty_start_date")
    private LocalDate warrantyStartDate;
    
    @Column(name = "warranty_end_date")
    private LocalDate warrantyEndDate;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_phone")
    private String customerPhone;
    
    @Column(name = "customer_email")
    private String customerEmail;
    
    @Column(name = "service_center_id")
    private Long serviceCenterId;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructor mặc định
    public Vehicle() {}
    
    // Constructor với các tham số cơ bản
    public Vehicle(String vin, String model, String brand, String customerName) {
        this.vin = vin;
        this.model = model;
        this.brand = brand;
        this.customerName = customerName;
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
    
    public String getVin() {
        return vin;
    }
    
    public void setVin(String vin) {
        this.vin = vin;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public LocalDate getManufactureDate() {
        return manufactureDate;
    }
    
    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }
    
    public LocalDate getWarrantyStartDate() {
        return warrantyStartDate;
    }
    
    public void setWarrantyStartDate(LocalDate warrantyStartDate) {
        this.warrantyStartDate = warrantyStartDate;
    }
    
    public LocalDate getWarrantyEndDate() {
        return warrantyEndDate;
    }
    
    public void setWarrantyEndDate(LocalDate warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public Long getServiceCenterId() {
        return serviceCenterId;
    }
    
    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
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
}