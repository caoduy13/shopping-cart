package com.oem.ev.service;

import com.oem.ev.model.Vehicle;
import com.oem.ev.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class cho Vehicle management
 * Cung cấp các business logic cho CRUD operations của Vehicle
 */
@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    /**
     * Tạo vehicle mới
     * @param vehicle Vehicle object cần tạo
     * @return Vehicle đã được tạo
     * @throws IllegalArgumentException nếu VIN đã tồn tại
     */
    public Vehicle createVehicle(Vehicle vehicle) {
        // Kiểm tra VIN đã tồn tại chưa
        if (vehicleRepository.existsByVin(vehicle.getVin())) {
            throw new IllegalArgumentException("VIN đã tồn tại: " + vehicle.getVin());
        }
        
        // Set timestamps
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setUpdatedAt(LocalDateTime.now());
        vehicle.setIsActive(true);
        
        // Lưu vehicle vào database
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Lấy vehicle theo ID
     * @param id ID của vehicle
     * @return Optional<Vehicle> - có thể null nếu không tìm thấy
     */
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
    
    /**
     * Lấy vehicle theo VIN
     * @param vin VIN của vehicle
     * @return Optional<Vehicle> - có thể null nếu không tìm thấy
     */
    public Optional<Vehicle> getVehicleByVin(String vin) {
        return vehicleRepository.findByVin(vin);
    }
    
    /**
     * Lấy tất cả vehicles
     * @return List<Vehicle> danh sách tất cả vehicles
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    
    /**
     * Lấy vehicles theo service center
     * @param serviceCenterId ID của service center
     * @return List<Vehicle> danh sách vehicles thuộc service center
     */
    public List<Vehicle> getVehiclesByServiceCenter(Long serviceCenterId) {
        return vehicleRepository.findByServiceCenterId(serviceCenterId);
    }
    
    /**
     * Lấy vehicles theo tên khách hàng
     * @param customerName tên khách hàng
     * @return List<Vehicle> danh sách vehicles của khách hàng
     */
    public List<Vehicle> getVehiclesByCustomerName(String customerName) {
        return vehicleRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
    
    /**
     * Lấy vehicles theo model
     * @param model model xe
     * @return List<Vehicle> danh sách vehicles theo model
     */
    public List<Vehicle> getVehiclesByModel(String model) {
        return vehicleRepository.findByModel(model);
    }
    
    /**
     * Lấy vehicles theo brand
     * @param brand thương hiệu xe
     * @return List<Vehicle> danh sách vehicles theo brand
     */
    public List<Vehicle> getVehiclesByBrand(String brand) {
        return vehicleRepository.findByBrand(brand);
    }
    
    /**
     * Lấy vehicles đang trong thời hạn bảo hành
     * @return List<Vehicle> danh sách vehicles còn bảo hành
     */
    public List<Vehicle> getVehiclesUnderWarranty() {
        return vehicleRepository.findVehiclesUnderWarranty(LocalDate.now());
    }
    
    /**
     * Lấy vehicles hết hạn bảo hành
     * @return List<Vehicle> danh sách vehicles hết bảo hành
     */
    public List<Vehicle> getVehiclesOutOfWarranty() {
        return vehicleRepository.findVehiclesOutOfWarranty(LocalDate.now());
    }
    
    /**
     * Lấy vehicles theo service center và trạng thái hoạt động
     * @param serviceCenterId ID của service center
     * @param isActive trạng thái hoạt động
     * @return List<Vehicle> danh sách vehicles thỏa mãn điều kiện
     */
    public List<Vehicle> getVehiclesByServiceCenterAndStatus(Long serviceCenterId, Boolean isActive) {
        return vehicleRepository.findByServiceCenterIdAndIsActive(serviceCenterId, isActive);
    }
    
    /**
     * Tìm vehicles theo nhiều tiêu chí
     * @param brand thương hiệu
     * @param model model
     * @param serviceCenterId ID service center
     * @return List<Vehicle> danh sách vehicles thỏa mãn điều kiện
     */
    public List<Vehicle> searchVehicles(String brand, String model, Long serviceCenterId) {
        return vehicleRepository.findVehiclesByCriteria(brand, model, serviceCenterId);
    }
    
    /**
     * Cập nhật vehicle
     * @param id ID của vehicle cần cập nhật
     * @param vehicleDetails thông tin vehicle mới
     * @return Vehicle đã được cập nhật
     * @throws IllegalArgumentException nếu vehicle không tồn tại
     */
    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        // Tìm vehicle hiện tại
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle không tồn tại với ID: " + id));
        
        // Kiểm tra VIN có thay đổi không và đã tồn tại chưa
        if (!existingVehicle.getVin().equals(vehicleDetails.getVin()) && 
            vehicleRepository.existsByVin(vehicleDetails.getVin())) {
            throw new IllegalArgumentException("VIN đã tồn tại: " + vehicleDetails.getVin());
        }
        
        // Cập nhật thông tin vehicle
        existingVehicle.setVin(vehicleDetails.getVin());
        existingVehicle.setModel(vehicleDetails.getModel());
        existingVehicle.setBrand(vehicleDetails.getBrand());
        existingVehicle.setManufactureDate(vehicleDetails.getManufactureDate());
        existingVehicle.setWarrantyStartDate(vehicleDetails.getWarrantyStartDate());
        existingVehicle.setWarrantyEndDate(vehicleDetails.getWarrantyEndDate());
        existingVehicle.setCustomerName(vehicleDetails.getCustomerName());
        existingVehicle.setCustomerPhone(vehicleDetails.getCustomerPhone());
        existingVehicle.setCustomerEmail(vehicleDetails.getCustomerEmail());
        existingVehicle.setServiceCenterId(vehicleDetails.getServiceCenterId());
        existingVehicle.setIsActive(vehicleDetails.getIsActive());
        
        // Cập nhật timestamp
        existingVehicle.setUpdatedAt(LocalDateTime.now());
        
        // Lưu vehicle đã cập nhật
        return vehicleRepository.save(existingVehicle);
    }
    
    /**
     * Xóa vehicle (soft delete)
     * @param id ID của vehicle cần xóa
     * @throws IllegalArgumentException nếu vehicle không tồn tại
     */
    public void deleteVehicle(Long id) {
        // Tìm vehicle hiện tại
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle không tồn tại với ID: " + id));
        
        // Soft delete - chỉ set isActive = false
        vehicle.setIsActive(false);
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        // Lưu thay đổi
        vehicleRepository.save(vehicle);
    }
    
    /**
     * Kích hoạt lại vehicle
     * @param id ID của vehicle cần kích hoạt
     * @return Vehicle đã được kích hoạt
     * @throws IllegalArgumentException nếu vehicle không tồn tại
     */
    public Vehicle activateVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle không tồn tại với ID: " + id));
        
        vehicle.setIsActive(true);
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        return vehicleRepository.save(vehicle);
    }
    
    /**
     * Cập nhật thông tin khách hàng của vehicle
     * @param id ID của vehicle
     * @param customerName tên khách hàng
     * @param customerPhone số điện thoại
     * @param customerEmail email
     * @return Vehicle đã được cập nhật
     * @throws IllegalArgumentException nếu vehicle không tồn tại
     */
    public Vehicle updateCustomerInfo(Long id, String customerName, String customerPhone, String customerEmail) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle không tồn tại với ID: " + id));
        
        vehicle.setCustomerName(customerName);
        vehicle.setCustomerPhone(customerPhone);
        vehicle.setCustomerEmail(customerEmail);
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        return vehicleRepository.save(vehicle);
    }
}