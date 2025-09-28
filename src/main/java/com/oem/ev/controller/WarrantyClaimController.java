package com.oem.ev.controller;

import com.oem.ev.model.WarrantyClaim;
import com.oem.ev.service.WarrantyClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller xử lý CRUD operations cho WarrantyClaim
 * Cung cấp các endpoint để quản lý warranty claims
 */
@RestController
@RequestMapping("/api/warranty-claims")
@CrossOrigin(origins = "*")
public class WarrantyClaimController {
    
    @Autowired
    private WarrantyClaimService warrantyClaimService;
    
    /**
     * Lấy tất cả warranty claims
     * @return List<WarrantyClaim> danh sách tất cả warranty claims
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> getAllWarrantyClaims() {
        List<WarrantyClaim> claims = warrantyClaimService.getAllWarrantyClaims();
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claim theo ID
     * @param id ID của warranty claim
     * @return WarrantyClaim object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> getWarrantyClaimById(@PathVariable Long id) {
        Optional<WarrantyClaim> claim = warrantyClaimService.getWarrantyClaimById(id);
        if (claim.isPresent()) {
            return ResponseEntity.ok(claim.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy warranty claim theo claim number
     * @param claimNumber số yêu cầu bảo hành
     * @return WarrantyClaim object hoặc 404 nếu không tìm thấy
     */
    @GetMapping("/claim-number/{claimNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> getWarrantyClaimByClaimNumber(@PathVariable String claimNumber) {
        Optional<WarrantyClaim> claim = warrantyClaimService.getWarrantyClaimByClaimNumber(claimNumber);
        if (claim.isPresent()) {
            return ResponseEntity.ok(claim.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy warranty claims theo trạng thái
     * @param status trạng thái của claim
     * @return List<WarrantyClaim> danh sách claims theo trạng thái
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> getWarrantyClaimsByStatus(@PathVariable WarrantyClaim.ClaimStatus status) {
        List<WarrantyClaim> claims = warrantyClaimService.getWarrantyClaimsByStatus(status);
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claims theo service center
     * @param serviceCenterId ID của service center
     * @return List<WarrantyClaim> danh sách claims của service center
     */
    @GetMapping("/service-center/{serviceCenterId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> getWarrantyClaimsByServiceCenter(@PathVariable Long serviceCenterId) {
        List<WarrantyClaim> claims = warrantyClaimService.getWarrantyClaimsByServiceCenter(serviceCenterId);
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claims theo kỹ thuật viên
     * @param technicianId ID của kỹ thuật viên
     * @return List<WarrantyClaim> danh sách claims được phân công cho kỹ thuật viên
     */
    @GetMapping("/technician/{technicianId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> getWarrantyClaimsByTechnician(@PathVariable Long technicianId) {
        List<WarrantyClaim> claims = warrantyClaimService.getWarrantyClaimsByTechnician(technicianId);
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claims theo xe
     * @param vehicleId ID của xe
     * @return List<WarrantyClaim> danh sách claims của xe
     */
    @GetMapping("/vehicle/{vehicleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> getWarrantyClaimsByVehicle(@PathVariable Long vehicleId) {
        List<WarrantyClaim> claims = warrantyClaimService.getWarrantyClaimsByVehicle(vehicleId);
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claims theo VIN xe
     * @param vehicleVin VIN của xe
     * @return List<WarrantyClaim> danh sách claims theo VIN
     */
    @GetMapping("/vehicle-vin/{vehicleVin}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> getWarrantyClaimsByVehicleVin(@PathVariable String vehicleVin) {
        List<WarrantyClaim> claims = warrantyClaimService.getWarrantyClaimsByVehicleVin(vehicleVin);
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claims chờ phê duyệt
     * @return List<WarrantyClaim> danh sách claims chờ phê duyệt
     */
    @GetMapping("/pending-approval")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<List<WarrantyClaim>> getPendingApprovalClaims() {
        List<WarrantyClaim> claims = warrantyClaimService.getPendingApprovalClaims();
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claims đang xử lý
     * @return List<WarrantyClaim> danh sách claims đang xử lý
     */
    @GetMapping("/in-progress")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> getInProgressClaims() {
        List<WarrantyClaim> claims = warrantyClaimService.getInProgressClaims();
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Lấy warranty claims đã hoàn thành trong khoảng thời gian
     * @param startDate ngày bắt đầu
     * @param endDate ngày kết thúc
     * @return List<WarrantyClaim> danh sách claims đã hoàn thành
     */
    @GetMapping("/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF')")
    public ResponseEntity<List<WarrantyClaim>> getCompletedClaimsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<WarrantyClaim> claims = warrantyClaimService.getCompletedClaimsByDateRange(startDate, endDate);
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Tìm warranty claims theo nhiều tiêu chí
     * @param serviceCenterId ID service center
     * @param status trạng thái
     * @param vehicleVin VIN xe
     * @return List<WarrantyClaim> danh sách claims thỏa mãn điều kiện
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<List<WarrantyClaim>> searchWarrantyClaims(
            @RequestParam(required = false) Long serviceCenterId,
            @RequestParam(required = false) WarrantyClaim.ClaimStatus status,
            @RequestParam(required = false) String vehicleVin) {
        List<WarrantyClaim> claims = warrantyClaimService.searchWarrantyClaims(serviceCenterId, status, vehicleVin);
        return ResponseEntity.ok(claims);
    }
    
    /**
     * Tạo warranty claim mới
     * @param warrantyClaim thông tin warranty claim mới
     * @return WarrantyClaim đã được tạo
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> createWarrantyClaim(@RequestBody WarrantyClaim warrantyClaim) {
        try {
            WarrantyClaim createdClaim = warrantyClaimService.createWarrantyClaim(warrantyClaim);
            return ResponseEntity.ok(createdClaim);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Tạo warranty claim thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật warranty claim
     * @param id ID của warranty claim cần cập nhật
     * @param warrantyClaimDetails thông tin warranty claim mới
     * @return WarrantyClaim đã được cập nhật
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF', 'SC_STAFF', 'SC_TECHNICIAN')")
    public ResponseEntity<?> updateWarrantyClaim(@PathVariable Long id, @RequestBody WarrantyClaim warrantyClaimDetails) {
        try {
            WarrantyClaim updatedClaim = warrantyClaimService.updateWarrantyClaim(id, warrantyClaimDetails);
            return ResponseEntity.ok(updatedClaim);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật warranty claim thất bại", e.getMessage()));
        }
    }
    
    /**
     * Cập nhật trạng thái warranty claim
     * @param id ID của warranty claim
     * @param statusRequest thông tin trạng thái mới
     * @return WarrantyClaim đã được cập nhật
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EVM_STAFF')")
    public ResponseEntity<?> updateClaimStatus(@PathVariable Long id, @RequestBody StatusRequest statusRequest) {
        try {
            WarrantyClaim updatedClaim = warrantyClaimService.updateClaimStatus(
                id, 
                statusRequest.getStatus(), 
                statusRequest.getApprovedBy()
            );
            return ResponseEntity.ok(updatedClaim);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Cập nhật trạng thái thất bại", e.getMessage()));
        }
    }
    
    /**
     * Phân công kỹ thuật viên cho warranty claim
     * @param id ID của warranty claim
     * @param assignmentRequest thông tin phân công
     * @return WarrantyClaim đã được cập nhật
     */
    @PutMapping("/{id}/assign-technician")
    @PreAuthorize("hasAnyRole('ADMIN', 'SC_STAFF')")
    public ResponseEntity<?> assignTechnician(@PathVariable Long id, @RequestBody TechnicianAssignmentRequest assignmentRequest) {
        try {
            WarrantyClaim updatedClaim = warrantyClaimService.assignTechnician(id, assignmentRequest.getTechnicianId());
            return ResponseEntity.ok(updatedClaim);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Phân công kỹ thuật viên thất bại", e.getMessage()));
        }
    }
    
    /**
     * Xóa warranty claim
     * @param id ID của warranty claim cần xóa
     * @return thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SC_STAFF')")
    public ResponseEntity<?> deleteWarrantyClaim(@PathVariable Long id) {
        try {
            warrantyClaimService.deleteWarrantyClaim(id);
            return ResponseEntity.ok(createSuccessResponse("Xóa warranty claim thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Xóa warranty claim thất bại", e.getMessage()));
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
     * Inner class cho status request
     */
    public static class StatusRequest {
        private WarrantyClaim.ClaimStatus status;
        private Long approvedBy;
        
        public StatusRequest() {}
        
        public StatusRequest(WarrantyClaim.ClaimStatus status, Long approvedBy) {
            this.status = status;
            this.approvedBy = approvedBy;
        }
        
        public WarrantyClaim.ClaimStatus getStatus() {
            return status;
        }
        
        public void setStatus(WarrantyClaim.ClaimStatus status) {
            this.status = status;
        }
        
        public Long getApprovedBy() {
            return approvedBy;
        }
        
        public void setApprovedBy(Long approvedBy) {
            this.approvedBy = approvedBy;
        }
    }
    
    /**
     * Inner class cho technician assignment request
     */
    public static class TechnicianAssignmentRequest {
        private Long technicianId;
        
        public TechnicianAssignmentRequest() {}
        
        public TechnicianAssignmentRequest(Long technicianId) {
            this.technicianId = technicianId;
        }
        
        public Long getTechnicianId() {
            return technicianId;
        }
        
        public void setTechnicianId(Long technicianId) {
            this.technicianId = technicianId;
        }
    }
}