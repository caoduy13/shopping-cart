package com.oem.ev.service;

import com.oem.ev.model.Part;
import com.oem.ev.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class cho Part management
 * Cung cấp các business logic cho CRUD operations của Part
 */
@Service
public class PartService {
    
    @Autowired
    private PartRepository partRepository;
    
    /**
     * Tạo part mới
     * @param part Part object cần tạo
     * @return Part đã được tạo
     * @throws IllegalArgumentException nếu part number đã tồn tại
     */
    public Part createPart(Part part) {
        // Kiểm tra part number đã tồn tại chưa
        if (partRepository.existsByPartNumber(part.getPartNumber())) {
            throw new IllegalArgumentException("Part number đã tồn tại: " + part.getPartNumber());
        }
        
        // Kiểm tra serial number đã tồn tại chưa (nếu có)
        if (part.getSerialNumber() != null && !part.getSerialNumber().isEmpty() && 
            partRepository.existsBySerialNumber(part.getSerialNumber())) {
            throw new IllegalArgumentException("Serial number đã tồn tại: " + part.getSerialNumber());
        }
        
        // Set timestamps
        part.setCreatedAt(LocalDateTime.now());
        part.setUpdatedAt(LocalDateTime.now());
        part.setIsActive(true);
        
        // Lưu part vào database
        return partRepository.save(part);
    }
    
    /**
     * Lấy part theo ID
     * @param id ID của part
     * @return Optional<Part> - có thể null nếu không tìm thấy
     */
    public Optional<Part> getPartById(Long id) {
        return partRepository.findById(id);
    }
    
    /**
     * Lấy part theo part number
     * @param partNumber mã phụ tùng
     * @return Optional<Part> - có thể null nếu không tìm thấy
     */
    public Optional<Part> getPartByPartNumber(String partNumber) {
        return partRepository.findByPartNumber(partNumber);
    }
    
    /**
     * Lấy part theo serial number
     * @param serialNumber số serial
     * @return Optional<Part> - có thể null nếu không tìm thấy
     */
    public Optional<Part> getPartBySerialNumber(String serialNumber) {
        return partRepository.findBySerialNumber(serialNumber);
    }
    
    /**
     * Lấy tất cả parts
     * @return List<Part> danh sách tất cả parts
     */
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }
    
    /**
     * Lấy parts theo loại
     * @param partType loại phụ tùng
     * @return List<Part> danh sách parts theo loại
     */
    public List<Part> getPartsByType(Part.PartType partType) {
        return partRepository.findByPartType(partType);
    }
    
    /**
     * Lấy parts theo VIN xe
     * @param vehicleVin VIN của xe
     * @return List<Part> danh sách parts được lắp trên xe
     */
    public List<Part> getPartsByVehicleVin(String vehicleVin) {
        return partRepository.findByVehicleVin(vehicleVin);
    }
    
    /**
     * Lấy parts có tồn kho thấp
     * @return List<Part> danh sách parts cần bổ sung
     */
    public List<Part> getPartsWithLowStock() {
        return partRepository.findPartsWithLowStock();
    }
    
    /**
     * Lấy parts theo tên (tìm kiếm gần đúng)
     * @param partName tên phụ tùng
     * @return List<Part> danh sách parts có tên chứa từ khóa
     */
    public List<Part> getPartsByName(String partName) {
        return partRepository.findByPartNameContainingIgnoreCase(partName);
    }
    
    /**
     * Lấy parts đang hoạt động
     * @return List<Part> danh sách parts đang hoạt động
     */
    public List<Part> getActiveParts() {
        return partRepository.findByIsActive(true);
    }
    
    /**
     * Lấy parts còn tồn kho
     * @return List<Part> danh sách parts còn tồn kho
     */
    public List<Part> getPartsInStock() {
        return partRepository.findPartsInStock();
    }
    
    /**
     * Tìm parts theo nhiều tiêu chí
     * @param partType loại phụ tùng
     * @param isActive trạng thái hoạt động
     * @return List<Part> danh sách parts thỏa mãn điều kiện
     */
    public List<Part> searchParts(Part.PartType partType, Boolean isActive) {
        return partRepository.findPartsByCriteria(partType, isActive);
    }
    
    /**
     * Cập nhật part
     * @param id ID của part cần cập nhật
     * @param partDetails thông tin part mới
     * @return Part đã được cập nhật
     * @throws IllegalArgumentException nếu part không tồn tại
     */
    public Part updatePart(Long id, Part partDetails) {
        // Tìm part hiện tại
        Part existingPart = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part không tồn tại với ID: " + id));
        
        // Kiểm tra part number có thay đổi không và đã tồn tại chưa
        if (!existingPart.getPartNumber().equals(partDetails.getPartNumber()) && 
            partRepository.existsByPartNumber(partDetails.getPartNumber())) {
            throw new IllegalArgumentException("Part number đã tồn tại: " + partDetails.getPartNumber());
        }
        
        // Kiểm tra serial number có thay đổi không và đã tồn tại chưa
        if (partDetails.getSerialNumber() != null && !partDetails.getSerialNumber().isEmpty() &&
            !existingPart.getSerialNumber().equals(partDetails.getSerialNumber()) && 
            partRepository.existsBySerialNumber(partDetails.getSerialNumber())) {
            throw new IllegalArgumentException("Serial number đã tồn tại: " + partDetails.getSerialNumber());
        }
        
        // Cập nhật thông tin part
        existingPart.setPartNumber(partDetails.getPartNumber());
        existingPart.setPartName(partDetails.getPartName());
        existingPart.setPartType(partDetails.getPartType());
        existingPart.setSerialNumber(partDetails.getSerialNumber());
        existingPart.setVehicleVin(partDetails.getVehicleVin());
        existingPart.setWarrantyPeriodMonths(partDetails.getWarrantyPeriodMonths());
        existingPart.setUnitPrice(partDetails.getUnitPrice());
        existingPart.setStockQuantity(partDetails.getStockQuantity());
        existingPart.setMinimumStockLevel(partDetails.getMinimumStockLevel());
        existingPart.setIsActive(partDetails.getIsActive());
        
        // Cập nhật timestamp
        existingPart.setUpdatedAt(LocalDateTime.now());
        
        // Lưu part đã cập nhật
        return partRepository.save(existingPart);
    }
    
    /**
     * Cập nhật tồn kho part
     * @param id ID của part
     * @param quantity số lượng mới
     * @return Part đã được cập nhật
     * @throws IllegalArgumentException nếu part không tồn tại
     */
    public Part updateStockQuantity(Long id, Integer quantity) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part không tồn tại với ID: " + id));
        
        part.setStockQuantity(quantity);
        part.setUpdatedAt(LocalDateTime.now());
        
        return partRepository.save(part);
    }
    
    /**
     * Cập nhật giá part
     * @param id ID của part
     * @param price giá mới
     * @return Part đã được cập nhật
     * @throws IllegalArgumentException nếu part không tồn tại
     */
    public Part updatePartPrice(Long id, BigDecimal price) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part không tồn tại với ID: " + id));
        
        part.setUnitPrice(price);
        part.setUpdatedAt(LocalDateTime.now());
        
        return partRepository.save(part);
    }
    
    /**
     * Gắn part vào xe
     * @param id ID của part
     * @param vehicleVin VIN của xe
     * @return Part đã được cập nhật
     * @throws IllegalArgumentException nếu part không tồn tại
     */
    public Part attachPartToVehicle(Long id, String vehicleVin) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part không tồn tại với ID: " + id));
        
        part.setVehicleVin(vehicleVin);
        part.setUpdatedAt(LocalDateTime.now());
        
        return partRepository.save(part);
    }
    
    /**
     * Tháo part khỏi xe
     * @param id ID của part
     * @return Part đã được cập nhật
     * @throws IllegalArgumentException nếu part không tồn tại
     */
    public Part detachPartFromVehicle(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part không tồn tại với ID: " + id));
        
        part.setVehicleVin(null);
        part.setUpdatedAt(LocalDateTime.now());
        
        return partRepository.save(part);
    }
    
    /**
     * Xóa part (soft delete)
     * @param id ID của part cần xóa
     * @throws IllegalArgumentException nếu part không tồn tại
     */
    public void deletePart(Long id) {
        // Tìm part hiện tại
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part không tồn tại với ID: " + id));
        
        // Soft delete - chỉ set isActive = false
        part.setIsActive(false);
        part.setUpdatedAt(LocalDateTime.now());
        
        // Lưu thay đổi
        partRepository.save(part);
    }
    
    /**
     * Kích hoạt lại part
     * @param id ID của part cần kích hoạt
     * @return Part đã được kích hoạt
     * @throws IllegalArgumentException nếu part không tồn tại
     */
    public Part activatePart(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Part không tồn tại với ID: " + id));
        
        part.setIsActive(true);
        part.setUpdatedAt(LocalDateTime.now());
        
        return partRepository.save(part);
    }
}