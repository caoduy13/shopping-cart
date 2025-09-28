package com.oem.ev.controller;

import com.oem.ev.model.Part;
import com.oem.ev.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller xử lý CRUD operations cho Part
 * Cung cấp các endpoint để quản lý parts
 */
@RestController
@RequestMapping("/api/parts")
@CrossOrigin(origins = "*")
public class PartController {
    
    @Autowired
    private PartService partService;
    
    /**
     * Lấy tất cả parts
     * @return List<Part> danh sách tất cả parts
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Part>> getAllParts() {
        List<Part> parts = partService.getAllParts();
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Lấy part theo ID
     * @param id ID của part
     * @return Part object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> getPartById(@PathVariable Long id) {
        Optional<Part> part = partService.getPartById(id);
        if (part.isPresent()) {
            return ResponseEntity.ok(part.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy part theo part number
     * @param partNumber mã phụ tùng
     * @return Part object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/part-number/{partNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> getPartByPartNumber(@PathVariable String partNumber) {
        Optional<Part> part = partService.getPartByPartNumber(partNumber);
        if (part.isPresent()) {
            return ResponseEntity.ok(part.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy part theo serial number
     * @param serialNumber số serial
     * @return Part object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/serial-number/{serialNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> getPartBySerialNumber(@PathVariable String serialNumber) {
        Optional<Part> part = partService.getPartBySerialNumber(serialNumber);
        if (part.isPresent()) {
            return ResponseEntity.ok(part.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy parts theo loại
     * @param partType loại phụ tùng
     * @return List<Part> danh sách parts theo loại
     */
    @GetMapping("/type/{partType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Part>> getPartsByType(@PathVariable Part.PartType partType) {
        List<Part> parts = partService.getPartsByType(partType);
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Lấy parts theo VIN xe
     * @param vehicleVin VIN của xe
     * @return List<Part> danh sách parts được lắp trên xe
     */
    @GetMapping("/vehicle/{vehicleVin}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Part>> getPartsByVehicleVin(@PathVariable String vehicleVin) {
        List<Part> parts = partService.getPartsByVehicleVin(vehicleVin);
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Lấy parts có tồn kho thấp
     * @return List<Part> danh sách parts cần bổ sung
     */
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<List<Part>> getPartsWithLowStock() {
        List<Part> parts = partService.getPartsWithLowStock();
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Lấy parts theo tên (tìm kiếm gần đúng)
     * @param partName tên phụ tùng
     * @return List<Part> danh sách parts có tên chứa từ khóa
     */
    @GetMapping("/search/{partName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Part>> getPartsByName(@PathVariable String partName) {
        List<Part> parts = partService.getPartsByName(partName);
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Lấy parts đang hoạt động
     * @return List<Part> danh sách parts đang hoạt động
     */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Part>> getActiveParts() {
        List<Part> parts = partService.getActiveParts();
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Lấy parts còn tồn kho
     * @return List<Part> danh sách parts còn tồn kho
     */
    @GetMapping("/in-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Part>> getPartsInStock() {
        List<Part> parts = partService.getPartsInStock();
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Tìm parts theo nhiều tiêu chí
     * @param partType loại phụ tùng
     * @param isActive trạng thái hoạt động
     * @return List<Part> danh sách parts thỏa mãn điều kiện
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Part>> searchParts(
            @RequestParam(required = false) Part.PartType partType,
            @RequestParam(required = false) Boolean isActive) {
        List<Part> parts = partService.searchParts(partType, isActive);
        return ResponseEntity.ok(parts);
    }
    
    /**
     * Tạo part mới
     * @param part thông tin part mới
     * @return Part đã được tạo
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> createPart(@RequestBody Part part) {
        try {
            Part createdPart = partService.createPart(part);
            return ResponseEntity.ok(createdPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Tạo part thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật part
     * @param id ID của part cần cập nhật
     * @param partDetails thông tin part mới
     * @return Part đã được cập nhật
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> updatePart(@PathVariable Long id, @RequestBody Part partDetails) {
        try {
            Part updatedPart = partService.updatePart(id, partDetails);
            return ResponseEntity.ok(updatedPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật part thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật tồn kho part
     * @param id ID của part
     * @param stockRequest thông tin tồn kho mới
     * @return Part đã được cập nhật
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<?> updateStockQuantity(@PathVariable Long id, @RequestBody StockRequest stockRequest) {
        try {
            Part updatedPart = partService.updateStockQuantity(id, stockRequest.getQuantity());
            return ResponseEntity.ok(updatedPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật tồn kho thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật giá part
     * @param id ID của part
     * @param priceRequest thông tin giá mới
     * @return Part đã được cập nhật
     */
    @PutMapping("/{id}/price")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> updatePartPrice(@PathVariable Long id, @RequestBody PriceRequest priceRequest) {
        try {
            Part updatedPart = partService.updatePartPrice(id, priceRequest.getPrice());
            return ResponseEntity.ok(updatedPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật giá thất bại", e.getMessage()));
        }
    }
    
    /**
     * Gắn part vào xe
     * @param id ID của part
     * @param vehicleRequest thông tin xe
     * @return Part đã được cập nhật
     */
    @PutMapping("/{id}/attach-vehicle")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<?> attachPartToVehicle(@PathVariable Long id, @RequestBody VehicleRequest vehicleRequest) {
        try {
            Part updatedPart = partService.attachPartToVehicle(id, vehicleRequest.getVehicleVin());
            return ResponseEntity.ok(updatedPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Gắn part vào xe thất bại", e.getMessage()));
        }
    }
    
    /**
     * Tháo part khỏi xe
     * @param id ID của part
     * @return Part đã được cập nhật
     */
    @PutMapping("/{id}/detach-vehicle")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<?> detachPartFromVehicle(@PathVariable Long id) {
        try {
            Part updatedPart = partService.detachPartFromVehicle(id);
            return ResponseEntity.ok(updatedPart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Tháo part khỏi xe thất bại", e.getMessage()));
        }
    }
    
    /**
     * Xóa part (soft delete)
     * @param id ID của part cần xóa
     * @return thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> deletePart(@PathVariable Long id) {
        try {
            partService.deletePart(id);
            return ResponseEntity.ok(createSuccessResponse("Xóa part thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Xóa part thất bại", e.getMessage()));
        }
    }
    
    /**
     * Kích hoạt part
     * @param id ID của part cần kích hoạt
     * @return Part đã được kích hoạt
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> activatePart(@PathVariable Long id) {
        try {
            Part part = partService.activatePart(id);
            return ResponseEntity.ok(part);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Kích hoạt part thất bại", e.getMessage()));
        }
    }
    
    /**
     * Tạo error response
     * @param error thông báo lỗi
     * @param message chi tiết lỗi
     * @return Map chứa thông tin lỗi
     */
    private Map<String, String> createErrorResponse(String error, String message) {
        return Map.of("error", error, "message", message);
    }
    
    /**
     * Tạo success response
     * @param message thông báo thành công
     * @return Map chứa thông báo thành công
     */
    private Map<String, String> createSuccessResponse(String message) {
        return Map.of("message", message);
    }
    
    /**
     * Inner class cho stock request
     */
    public static class StockRequest {
        private Integer quantity;
        
        public StockRequest() {}
        
        public StockRequest(Integer quantity) {
            this.quantity = quantity;
        }
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
    
    /**
     * Inner class cho price request
     */
    public static class PriceRequest {
        private BigDecimal price;
        
        public PriceRequest() {}
        
        public PriceRequest(BigDecimal price) {
            this.price = price;
        }
        
        public BigDecimal getPrice() {
            return price;
        }
        
        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
    
    /**
     * Inner class cho vehicle request
     */
    public static class VehicleRequest {
        private String vehicleVin;
        
        public VehicleRequest() {}
        
        public VehicleRequest(String vehicleVin) {
            this.vehicleVin = vehicleVin;
        }
        
        public String getVehicleVin() {
            return vehicleVin;
        }
        
        public void setVehicleVin(String vehicleVin) {
            this.vehicleVin = vehicleVin;
        }
    }
}