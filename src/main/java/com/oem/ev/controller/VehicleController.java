package com.oem.ev.controller;

import com.oem.ev.model.Vehicle;
import com.oem.ev.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller xử lý CRUD operations cho Vehicle
 * Cung cấp các endpoint để quản lý vehicles
 */
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    /**
     * Lấy tất cả vehicles
     * @return List<Vehicle> danh sách tất cả vehicles
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy vehicle theo ID
     * @param id ID của vehicle
     * @return Vehicle object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleById(id);
        if (vehicle.isPresent()) {
            return ResponseEntity.ok(vehicle.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy vehicle theo VIN
     * @param vin VIN của vehicle
     * @return Vehicle object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/vin/{vin}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> getVehicleByVin(@PathVariable String vin) {
        Optional<Vehicle> vehicle = vehicleService.getVehicleByVin(vin);
        if (vehicle.isPresent()) {
            return ResponseEntity.ok(vehicle.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy vehicles theo service center
     * @param serviceCenterId ID của service center
     * @return List<Vehicle> danh sách vehicles thuộc service center
     */
    @GetMapping("/service-center/{serviceCenterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> getVehiclesByServiceCenter(@PathVariable Long serviceCenterId) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByServiceCenter(serviceCenterId);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy vehicles theo tên khách hàng
     * @param customerName tên khách hàng
     * @return List<Vehicle> danh sách vehicles của khách hàng
     */
    @GetMapping("/customer/{customerName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> getVehiclesByCustomerName(@PathVariable String customerName) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByCustomerName(customerName);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy vehicles theo model
     * @param model model xe
     * @return List<Vehicle> danh sách vehicles theo model
     */
    @GetMapping("/model/{model}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> getVehiclesByModel(@PathVariable String model) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByModel(model);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy vehicles theo brand
     * @param brand thương hiệu xe
     * @return List<Vehicle> danh sách vehicles theo brand
     */
    @GetMapping("/brand/{brand}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> getVehiclesByBrand(@PathVariable String brand) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByBrand(brand);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy vehicles đang trong thời hạn bảo hành
     * @return List<Vehicle> danh sách vehicles còn bảo hành
     */
    @GetMapping("/under-warranty")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> getVehiclesUnderWarranty() {
        List<Vehicle> vehicles = vehicleService.getVehiclesUnderWarranty();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Lấy vehicles hết hạn bảo hành
     * @return List<Vehicle> danh sách vehicles hết bảo hành
     */
    @GetMapping("/out-of-warranty")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> getVehiclesOutOfWarranty() {
        List<Vehicle> vehicles = vehicleService.getVehiclesOutOfWarranty();
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Tìm vehicles theo nhiều tiêu chí
     * @param brand thương hiệu
     * @param model model
     * @param serviceCenterId ID service center
     * @return List<Vehicle> danh sách vehicles thỏa mãn điều kiện
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<Vehicle>> searchVehicles(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Long serviceCenterId) {
        List<Vehicle> vehicles = vehicleService.searchVehicles(brand, model, serviceCenterId);
        return ResponseEntity.ok(vehicles);
    }
    
    /**
     * Tạo vehicle mới
     * @param vehicle thông tin vehicle mới
     * @return Vehicle đã được tạo
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<?> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle createdVehicle = vehicleService.createVehicle(vehicle);
            return ResponseEntity.ok(createdVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Tạo vehicle thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật vehicle
     * @param id ID của vehicle cần cập nhật
     * @param vehicleDetails thông tin vehicle mới
     * @return Vehicle đã được cập nhật
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        try {
            Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleDetails);
            return ResponseEntity.ok(updatedVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật vehicle thất bại", e.getMessage()));
        }
    }
    
    /**
     * Xóa vehicle (soft delete)
     * @param id ID của vehicle cần xóa
     * @return thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        try {
            vehicleService.deleteVehicle(id);
            return ResponseEntity.ok(createSuccessResponse("Xóa vehicle thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Xóa vehicle thất bại", e.getMessage()));
        }
    }
    
    /**
     * Kích hoạt vehicle
     * @param id ID của vehicle cần kích hoạt
     * @return Vehicle đã được kích hoạt
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> activateVehicle(@PathVariable Long id) {
        try {
            Vehicle vehicle = vehicleService.activateVehicle(id);
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Kích hoạt vehicle thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật thông tin khách hàng
     * @param id ID của vehicle
     * @param customerInfo thông tin khách hàng mới
     * @return Vehicle đã được cập nhật
     */
    @PutMapping("/{id}/customer")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<?> updateCustomerInfo(@PathVariable Long id, @RequestBody CustomerInfo customerInfo) {
        try {
            Vehicle vehicle = vehicleService.updateCustomerInfo(
                id, 
                customerInfo.getCustomerName(), 
                customerInfo.getCustomerPhone(), 
                customerInfo.getCustomerEmail()
            );
            return ResponseEntity.ok(vehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật thông tin khách hàng thất bại", e.getMessage()));
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
     * Inner class cho customer info request
     */
    public static class CustomerInfo {
        private String customerName;
        private String customerPhone;
        private String customerEmail;
        
        public CustomerInfo() {}
        
        public CustomerInfo(String customerName, String customerPhone, String customerEmail) {
            this.customerName = customerName;
            this.customerPhone = customerPhone;
            this.customerEmail = customerEmail;
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
    }
}