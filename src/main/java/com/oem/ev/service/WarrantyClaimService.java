package com.oem.ev.service;

import com.oem.ev.model.WarrantyClaim;
import com.oem.ev.repository.WarrantyClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class cho WarrantyClaim management
 * Cung cấp các business logic cho CRUD operations của WarrantyClaim
 */
@Service
public class WarrantyClaimService {
    
    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;
    
    /**
     * Tạo warranty claim mới
     * @param warrantyClaim WarrantyClaim object cần tạo
     * @return WarrantyClaim đã được tạo
     * @throws IllegalArgumentException nếu claim number đã tồn tại
     */
    public WarrantyClaim createWarrantyClaim(WarrantyClaim warrantyClaim) {
        // Tạo claim number tự động nếu chưa có
        if (warrantyClaim.getClaimNumber() == null || warrantyClaim.getClaimNumber().isEmpty()) {
            warrantyClaim.setClaimNumber(generateClaimNumber());
        }
        
        // Kiểm tra claim number đã tồn tại chưa
        if (warrantyClaimRepository.existsByClaimNumber(warrantyClaim.getClaimNumber())) {
            throw new IllegalArgumentException("Claim number đã tồn tại: " + warrantyClaim.getClaimNumber());
        }
        
        // Set timestamps và status mặc định
        warrantyClaim.setStatus(WarrantyClaim.ClaimStatus.SUBMITTED);
        warrantyClaim.setCreatedAt(LocalDateTime.now());
        warrantyClaim.setUpdatedAt(LocalDateTime.now());
        
        // Lưu warranty claim vào database
        return warrantyClaimRepository.save(warrantyClaim);
    }
    
    /**
     * Lấy warranty claim theo ID
     * @param id ID của warranty claim
     * @return Optional<WarrantyClaim> - có thể null nếu không tìm thấy
     */
    public Optional<WarrantyClaim> getWarrantyClaimById(Long id) {
        return warrantyClaimRepository.findById(id);
    }
    
    /**
     * Lấy warranty claim theo claim number
     * @param claimNumber số yêu cầu bảo hành
     * @return Optional<WarrantyClaim> - có thể null nếu không tìm thấy
     */
    public Optional<WarrantyClaim> getWarrantyClaimByClaimNumber(String claimNumber) {
        return warrantyClaimRepository.findByClaimNumber(claimNumber);
    }
    
    /**
     * Lấy tất cả warranty claims
     * @return List<WarrantyClaim> danh sách tất cả warranty claims
     */
    public List<WarrantyClaim> getAllWarrantyClaims() {
        return warrantyClaimRepository.findAll();
    }
    
    /**
     * Lấy warranty claims theo trạng thái
     * @param status trạng thái của claim
     * @return List<WarrantyClaim> danh sách claims theo trạng thái
     */
    public List<WarrantyClaim> getWarrantyClaimsByStatus(WarrantyClaim.ClaimStatus status) {
        return warrantyClaimRepository.findByStatus(status);
    }
    
    /**
     * Lấy warranty claims theo service center
     * @param serviceCenterId ID của service center
     * @return List<WarrantyClaim> danh sách claims của service center
     */
    public List<WarrantyClaim> getWarrantyClaimsByServiceCenter(Long serviceCenterId) {
        return warrantyClaimRepository.findByServiceCenterId(serviceCenterId);
    }
    
    /**
     * Lấy warranty claims theo kỹ thuật viên
     * @param technicianId ID của kỹ thuật viên
     * @return List<WarrantyClaim> danh sách claims được phân công cho kỹ thuật viên
     */
    public List<WarrantyClaim> getWarrantyClaimsByTechnician(Long technicianId) {
        return warrantyClaimRepository.findByTechnicianId(technicianId);
    }
    
    /**
     * Lấy warranty claims theo xe
     * @param vehicleId ID của xe
     * @return List<WarrantyClaim> danh sách claims của xe
     */
    public List<WarrantyClaim> getWarrantyClaimsByVehicle(Long vehicleId) {
        return warrantyClaimRepository.findByVehicleId(vehicleId);
    }
    
    /**
     * Lấy warranty claims theo VIN xe
     * @param vehicleVin VIN của xe
     * @return List<WarrantyClaim> danh sách claims theo VIN
     */
    public List<WarrantyClaim> getWarrantyClaimsByVehicleVin(String vehicleVin) {
        return warrantyClaimRepository.findByVehicleVin(vehicleVin);
    }
    
    /**
     * Lấy warranty claims chờ phê duyệt
     * @return List<WarrantyClaim> danh sách claims chờ phê duyệt
     */
    public List<WarrantyClaim> getPendingApprovalClaims() {
        return warrantyClaimRepository.findPendingApprovalClaims();
    }
    
    /**
     * Lấy warranty claims đang xử lý
     * @return List<WarrantyClaim> danh sách claims đang xử lý
     */
    public List<WarrantyClaim> getInProgressClaims() {
        return warrantyClaimRepository.findInProgressClaims();
    }
    
    /**
     * Lấy warranty claims đã hoàn thành trong khoảng thời gian
     * @param startDate ngày bắt đầu
     * @param endDate ngày kết thúc
     * @return List<WarrantyClaim> danh sách claims đã hoàn thành
     */
    public List<WarrantyClaim> getCompletedClaimsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return warrantyClaimRepository.findCompletedClaimsByDateRange(startDate, endDate);
    }
    
    /**
     * Tìm warranty claims theo nhiều tiêu chí
     * @param serviceCenterId ID service center
     * @param status trạng thái
     * @param vehicleVin VIN xe
     * @return List<WarrantyClaim> danh sách claims thỏa mãn điều kiện
     */
    public List<WarrantyClaim> searchWarrantyClaims(Long serviceCenterId, WarrantyClaim.ClaimStatus status, String vehicleVin) {
        return warrantyClaimRepository.findClaimsByCriteria(serviceCenterId, status, vehicleVin);
    }
    
    /**
     * Cập nhật warranty claim
     * @param id ID của warranty claim cần cập nhật
     * @param warrantyClaimDetails thông tin warranty claim mới
     * @return WarrantyClaim đã được cập nhật
     * @throws IllegalArgumentException nếu warranty claim không tồn tại
     */
    public WarrantyClaim updateWarrantyClaim(Long id, WarrantyClaim warrantyClaimDetails) {
        // Tìm warranty claim hiện tại
        WarrantyClaim existingClaim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warranty claim không tồn tại với ID: " + id));
        
        // Kiểm tra claim number có thay đổi không và đã tồn tại chưa
        if (!existingClaim.getClaimNumber().equals(warrantyClaimDetails.getClaimNumber()) && 
            warrantyClaimRepository.existsByClaimNumber(warrantyClaimDetails.getClaimNumber())) {
            throw new IllegalArgumentException("Claim number đã tồn tại: " + warrantyClaimDetails.getClaimNumber());
        }
        
        // Cập nhật thông tin warranty claim
        existingClaim.setClaimNumber(warrantyClaimDetails.getClaimNumber());
        existingClaim.setVehicleId(warrantyClaimDetails.getVehicleId());
        existingClaim.setVehicleVin(warrantyClaimDetails.getVehicleVin());
        existingClaim.setServiceCenterId(warrantyClaimDetails.getServiceCenterId());
        existingClaim.setTechnicianId(warrantyClaimDetails.getTechnicianId());
        existingClaim.setProblemDescription(warrantyClaimDetails.getProblemDescription());
        existingClaim.setDiagnosticReport(warrantyClaimDetails.getDiagnosticReport());
        existingClaim.setInspectionImages(warrantyClaimDetails.getInspectionImages());
        existingClaim.setEstimatedCost(warrantyClaimDetails.getEstimatedCost());
        existingClaim.setActualCost(warrantyClaimDetails.getActualCost());
        existingClaim.setPartsRequired(warrantyClaimDetails.getPartsRequired());
        existingClaim.setWorkPerformed(warrantyClaimDetails.getWorkPerformed());
        
        // Cập nhật timestamp
        existingClaim.setUpdatedAt(LocalDateTime.now());
        
        // Lưu warranty claim đã cập nhật
        return warrantyClaimRepository.save(existingClaim);
    }
    
    /**
     * Cập nhật trạng thái warranty claim
     * @param id ID của warranty claim
     * @param status trạng thái mới
     * @param approvedBy ID của user phê duyệt (nếu có)
     * @return WarrantyClaim đã được cập nhật
     * @throws IllegalArgumentException nếu warranty claim không tồn tại
     */
    public WarrantyClaim updateClaimStatus(Long id, WarrantyClaim.ClaimStatus status, Long approvedBy) {
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warranty claim không tồn tại với ID: " + id));
        
        claim.setStatus(status);
        claim.setUpdatedAt(LocalDateTime.now());
        
        // Nếu được phê duyệt, set thông tin phê duyệt
        if (status == WarrantyClaim.ClaimStatus.APPROVED) {
            claim.setApprovedBy(approvedBy);
            claim.setApprovedAt(LocalDateTime.now());
        }
        
        // Nếu hoàn thành, set thời gian hoàn thành
        if (status == WarrantyClaim.ClaimStatus.COMPLETED) {
            claim.setCompletionDate(LocalDateTime.now());
        }
        
        return warrantyClaimRepository.save(claim);
    }
    
    /**
     * Phân công kỹ thuật viên cho warranty claim
     * @param id ID của warranty claim
     * @param technicianId ID của kỹ thuật viên
     * @return WarrantyClaim đã được cập nhật
     * @throws IllegalArgumentException nếu warranty claim không tồn tại
     */
    public WarrantyClaim assignTechnician(Long id, Long technicianId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warranty claim không tồn tại với ID: " + id));
        
        claim.setTechnicianId(technicianId);
        claim.setUpdatedAt(LocalDateTime.now());
        
        return warrantyClaimRepository.save(claim);
    }
    
    /**
     * Xóa warranty claim (soft delete)
     * @param id ID của warranty claim cần xóa
     * @throws IllegalArgumentException nếu warranty claim không tồn tại
     */
    public void deleteWarrantyClaim(Long id) {
        // Tìm warranty claim hiện tại
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warranty claim không tồn tại với ID: " + id));
        
        // Chỉ cho phép xóa nếu claim chưa được phê duyệt
        if (claim.getStatus() != WarrantyClaim.ClaimStatus.SUBMITTED) {
            throw new IllegalArgumentException("Không thể xóa warranty claim đã được phê duyệt");
        }
        
        // Xóa warranty claim
        warrantyClaimRepository.delete(claim);
    }
    
    /**
     * Tạo claim number tự động
     * @return claim number string
     */
    private String generateClaimNumber() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "WC-" + timestamp.substring(timestamp.length() - 8) + "-" + uuid.toUpperCase();
    }
}