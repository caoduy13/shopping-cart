package com.oem.ev.repository;

import com.oem.ev.model.WarrantyClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho WarrantyClaim entity
 * Cung cấp các method để truy vấn thông tin yêu cầu bảo hành
 */
@Repository
public interface WarrantyClaimRepository extends JpaRepository<WarrantyClaim, Long> {
    
    /**
     * Tìm warranty claim theo claim number
     * @param claimNumber số yêu cầu bảo hành
     * @return Optional<WarrantyClaim> - có thể null nếu không tìm thấy
     */
    Optional<WarrantyClaim> findByClaimNumber(String claimNumber);
    
    /**
     * Kiểm tra claim number đã tồn tại chưa
     * @param claimNumber số yêu cầu bảo hành
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByClaimNumber(String claimNumber);
    
    /**
     * Tìm tất cả claim theo trạng thái
     * @param status trạng thái của claim
     * @return List<WarrantyClaim> danh sách claim theo trạng thái
     */
    List<WarrantyClaim> findByStatus(WarrantyClaim.ClaimStatus status);
    
    /**
     * Tìm tất cả claim theo service center
     * @param serviceCenterId ID của service center
     * @return List<WarrantyClaim> danh sách claim của service center
     */
    List<WarrantyClaim> findByServiceCenterId(Long serviceCenterId);
    
    /**
     * Tìm tất cả claim theo kỹ thuật viên
     * @param technicianId ID của kỹ thuật viên
     * @return List<WarrantyClaim> danh sách claim được phân công cho kỹ thuật viên
     */
    List<WarrantyClaim> findByTechnicianId(Long technicianId);
    
    /**
     * Tìm tất cả claim theo xe
     * @param vehicleId ID của xe
     * @return List<WarrantyClaim> danh sách claim của xe
     */
    List<WarrantyClaim> findByVehicleId(Long vehicleId);
    
    /**
     * Tìm tất cả claim theo VIN xe
     * @param vehicleVin VIN của xe
     * @return List<WarrantyClaim> danh sách claim theo VIN
     */
    List<WarrantyClaim> findByVehicleVin(String vehicleVin);
    
    /**
     * Tìm claim theo service center và trạng thái
     * @param serviceCenterId ID của service center
     * @param status trạng thái của claim
     * @return List<WarrantyClaim> danh sách claim thỏa mãn điều kiện
     */
    List<WarrantyClaim> findByServiceCenterIdAndStatus(Long serviceCenterId, WarrantyClaim.ClaimStatus status);
    
    /**
     * Tìm claim theo kỹ thuật viên và trạng thái
     * @param technicianId ID của kỹ thuật viên
     * @param status trạng thái của claim
     * @return List<WarrantyClaim> danh sách claim thỏa mãn điều kiện
     */
    List<WarrantyClaim> findByTechnicianIdAndStatus(Long technicianId, WarrantyClaim.ClaimStatus status);
    
    /**
     * Custom query để tìm claim trong khoảng thời gian
     * @param startDate ngày bắt đầu
     * @param endDate ngày kết thúc
     * @return List<WarrantyClaim> danh sách claim trong khoảng thời gian
     */
    @Query("SELECT wc FROM WarrantyClaim wc WHERE wc.createdAt BETWEEN :startDate AND :endDate")
    List<WarrantyClaim> findClaimsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    /**
     * Custom query để tìm claim chờ phê duyệt
     * @return List<WarrantyClaim> danh sách claim chờ phê duyệt
     */
    @Query("SELECT wc FROM WarrantyClaim wc WHERE wc.status = 'PENDING_APPROVAL'")
    List<WarrantyClaim> findPendingApprovalClaims();
    
    /**
     * Custom query để tìm claim đang xử lý
     * @return List<WarrantyClaim> danh sách claim đang xử lý
     */
    @Query("SELECT wc FROM WarrantyClaim wc WHERE wc.status = 'IN_PROGRESS'")
    List<WarrantyClaim> findInProgressClaims();
    
    /**
     * Custom query để tìm claim đã hoàn thành trong khoảng thời gian
     * @param startDate ngày bắt đầu
     * @param endDate ngày kết thúc
     * @return List<WarrantyClaim> danh sách claim đã hoàn thành
     */
    @Query("SELECT wc FROM WarrantyClaim wc WHERE wc.status = 'COMPLETED' " +
           "AND wc.completionDate BETWEEN :startDate AND :endDate")
    List<WarrantyClaim> findCompletedClaimsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Custom query để thống kê claim theo trạng thái
     * @return List<Object[]> danh sách thống kê [status, count]
     */
    @Query("SELECT wc.status, COUNT(wc) FROM WarrantyClaim wc GROUP BY wc.status")
    List<Object[]> getClaimStatisticsByStatus();
    
    /**
     * Custom query để tìm claim theo nhiều tiêu chí
     * @param serviceCenterId ID service center
     * @param status trạng thái
     * @param vehicleVin VIN xe
     * @return List<WarrantyClaim> danh sách claim thỏa mãn điều kiện
     */
    @Query("SELECT wc FROM WarrantyClaim wc WHERE " +
           "(:serviceCenterId IS NULL OR wc.serviceCenterId = :serviceCenterId) AND " +
           "(:status IS NULL OR wc.status = :status) AND " +
           "(:vehicleVin IS NULL OR wc.vehicleVin = :vehicleVin)")
    List<WarrantyClaim> findClaimsByCriteria(@Param("serviceCenterId") Long serviceCenterId,
                                            @Param("status") WarrantyClaim.ClaimStatus status,
                                            @Param("vehicleVin") String vehicleVin);
}