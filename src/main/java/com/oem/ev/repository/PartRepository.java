package com.oem.ev.repository;

import com.oem.ev.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho Part entity
 * Cung cấp các method để truy vấn thông tin phụ tùng
 */
@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    
    /**
     * Tìm phụ tùng theo part number
     * @param partNumber mã phụ tùng
     * @return Optional<Part> - có thể null nếu không tìm thấy
     */
    Optional<Part> findByPartNumber(String partNumber);
    
    /**
     * Tìm phụ tùng theo serial number
     * @param serialNumber số serial
     * @return Optional<Part> - có thể null nếu không tìm thấy
     */
    Optional<Part> findBySerialNumber(String serialNumber);
    
    /**
     * Kiểm tra part number đã tồn tại chưa
     * @param partNumber mã phụ tùng
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByPartNumber(String partNumber);
    
    /**
     * Kiểm tra serial number đã tồn tại chưa
     * @param serialNumber số serial
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsBySerialNumber(String serialNumber);
    
    /**
     * Tìm tất cả phụ tùng theo loại
     * @param partType loại phụ tùng
     * @return List<Part> danh sách phụ tùng theo loại
     */
    List<Part> findByPartType(Part.PartType partType);
    
    /**
     * Tìm phụ tùng theo VIN xe
     * @param vehicleVin VIN của xe
     * @return List<Part> danh sách phụ tùng được lắp trên xe
     */
    List<Part> findByVehicleVin(String vehicleVin);
    
    /**
     * Tìm phụ tùng có tồn kho thấp
     * @return List<Part> danh sách phụ tùng cần bổ sung
     */
    @Query("SELECT p FROM Part p WHERE p.stockQuantity <= p.minimumStockLevel")
    List<Part> findPartsWithLowStock();
    
    /**
     * Tìm phụ tùng theo tên (tìm kiếm gần đúng)
     * @param partName tên phụ tùng
     * @return List<Part> danh sách phụ tùng có tên chứa từ khóa
     */
    List<Part> findByPartNameContainingIgnoreCase(String partName);
    
    /**
     * Tìm phụ tùng đang hoạt động
     * @param isActive trạng thái hoạt động
     * @return List<Part> danh sách phụ tùng đang hoạt động
     */
    List<Part> findByIsActive(Boolean isActive);
    
    /**
     * Custom query để tìm phụ tùng theo nhiều tiêu chí
     * @param partType loại phụ tùng
     * @param isActive trạng thái hoạt động
     * @return List<Part> danh sách phụ tùng thỏa mãn điều kiện
     */
    @Query("SELECT p FROM Part p WHERE " +
           "(:partType IS NULL OR p.partType = :partType) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive)")
    List<Part> findPartsByCriteria(@Param("partType") Part.PartType partType, 
                                   @Param("isActive") Boolean isActive);
    
    /**
     * Custom query để tìm phụ tùng có tồn kho
     * @return List<Part> danh sách phụ tùng còn tồn kho
     */
    @Query("SELECT p FROM Part p WHERE p.stockQuantity > 0")
    List<Part> findPartsInStock();
}