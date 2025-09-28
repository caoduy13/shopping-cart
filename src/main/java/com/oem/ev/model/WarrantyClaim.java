package com.oem.ev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity class đại diện cho WarrantyClaim (Yêu cầu bảo hành) trong hệ thống
 * Lưu trữ thông tin các yêu cầu bảo hành từ trung tâm dịch vụ
 */
@Entity
@Table(name = "warranty_claims")
public class WarrantyClaim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Claim number không được để trống")
    @Column(name = "claim_number", unique = true, nullable = false)
    private String claimNumber;
    
    @NotNull(message = "Vehicle ID không được null")
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;
    
    @Column(name = "vehicle_vin")
    private String vehicleVin;
    
    @NotNull(message = "Service center ID không được null")
    @Column(name = "service_center_id", nullable = false)
    private Long serviceCenterId;
    
    @Column(name = "technician_id")
    private Long technicianId; // ID của kỹ thuật viên được phân công
    
    @NotBlank(message = "Problem description không được để trống")
    @Column(name = "problem_description", columnDefinition = "TEXT")
    private String problemDescription;
    
    @Column(name = "diagnostic_report", columnDefinition = "TEXT")
    private String diagnosticReport;
    
    @Column(name = "inspection_images", columnDefinition = "TEXT")
    private String inspectionImages; // JSON string chứa danh sách URL hình ảnh
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;
    
    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;
    
    @Column(name = "actual_cost", precision = 10, scale = 2)
    private BigDecimal actualCost;
    
    @Column(name = "parts_required", columnDefinition = "TEXT")
    private String partsRequired; // JSON string chứa danh sách phụ tùng cần thay thế
    
    @Column(name = "work_performed", columnDefinition = "TEXT")
    private String workPerformed; // Mô tả công việc đã thực hiện
    
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    
    @Column(name = "created_by")
    private Long createdBy; // ID của user tạo claim
    
    @Column(name = "approved_by")
    private Long approvedBy; // ID của user phê duyệt claim
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructor mặc định
    public WarrantyClaim() {}
    
    // Constructor với các tham số cơ bản
    public WarrantyClaim(String claimNumber, Long vehicleId, String vehicleVin, 
                        Long serviceCenterId, String problemDescription) {
        this.claimNumber = claimNumber;
        this.vehicleId = vehicleId;
        this.vehicleVin = vehicleVin;
        this.serviceCenterId = serviceCenterId;
        this.problemDescription = problemDescription;
        this.status = ClaimStatus.SUBMITTED;
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
    
    public String getClaimNumber() {
        return claimNumber;
    }
    
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }
    
    public Long getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
    
    public String getVehicleVin() {
        return vehicleVin;
    }
    
    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }
    
    public Long getServiceCenterId() {
        return serviceCenterId;
    }
    
    public void setServiceCenterId(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }
    
    public Long getTechnicianId() {
        return technicianId;
    }
    
    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }
    
    public String getProblemDescription() {
        return problemDescription;
    }
    
    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }
    
    public String getDiagnosticReport() {
        return diagnosticReport;
    }
    
    public void setDiagnosticReport(String diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
    }
    
    public String getInspectionImages() {
        return inspectionImages;
    }
    
    public void setInspectionImages(String inspectionImages) {
        this.inspectionImages = inspectionImages;
    }
    
    public ClaimStatus getStatus() {
        return status;
    }
    
    public void setStatus(ClaimStatus status) {
        this.status = status;
    }
    
    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }
    
    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
    
    public BigDecimal getActualCost() {
        return actualCost;
    }
    
    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }
    
    public String getPartsRequired() {
        return partsRequired;
    }
    
    public void setPartsRequired(String partsRequired) {
        this.partsRequired = partsRequired;
    }
    
    public String getWorkPerformed() {
        return workPerformed;
    }
    
    public void setWorkPerformed(String workPerformed) {
        this.workPerformed = workPerformed;
    }
    
    public LocalDateTime getCompletionDate() {
        return completionDate;
    }
    
    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public Long getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }
    
    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
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
     * Enum định nghĩa các trạng thái của warranty claim
     */
    public enum ClaimStatus {
        SUBMITTED("Đã gửi"),              // Yêu cầu đã được gửi
        PENDING_APPROVAL("Chờ duyệt"),    // Chờ phê duyệt từ hãng
        APPROVED("Được chấp nhận"),       // Đã được phê duyệt
        IN_PROGRESS("Đang xử lý"),        // Đang thực hiện bảo hành
        COMPLETED("Đã hoàn thành"),       // Đã hoàn thành
        REJECTED("Bị từ chối"),           // Bị từ chối
        CANCELLED("Đã hủy");              // Đã hủy
        
        private final String displayName;
        
        ClaimStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}