package com.oem.ev.repository;

import com.oem.ev.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho Vehicle entity
 * Cung cấp các method để truy vấn thông tin xe
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    /**
     * Tìm xe theo VIN
     * @param vin Vehicle Identification Number
     * @return Optional<Vehicle> - có thể null nếu không tìm thấy
     */
    Optional<Vehicle> findByVin(String vin);
    
    /**
     * Kiểm tra VIN đã tồn tại chưa
     * @param vin Vehicle Identification Number
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByVin(String vin);
    
    /**
     * Tìm tất cả xe theo service center
     * @param serviceCenterId ID của service center
     * @return List<Vehicle> danh sách xe thuộc service center
     */
    List<Vehicle> findByServiceCenterId(Long serviceCenterId);
    
    /**
     * Tìm xe theo tên khách hàng
     * @param customerName tên khách hàng
     * @return List<Vehicle> danh sách xe của khách hàng
     */
    List<Vehicle> findByCustomerNameContainingIgnoreCase(String customerName);
    
    /**
     * Tìm xe theo model
     * @param model model xe
     * @return List<Vehicle> danh sách xe theo model
     */
    List<Vehicle> findByModel(String model);
    
    /**
     * Tìm xe theo brand
     * @param brand thương hiệu xe
     * @return List<Vehicle> danh sách xe theo brand
     */
    List<Vehicle> findByBrand(String brand);
    
    /**
     * Tìm xe đang trong thời hạn bảo hành
     * @param currentDate ngày hiện tại
     * @return List<Vehicle> danh sách xe còn bảo hành
     */
    @Query("SELECT v FROM Vehicle v WHERE v.warrantyEndDate >= :currentDate")
    List<Vehicle> findVehiclesUnderWarranty(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Tìm xe hết hạn bảo hành
     * @param currentDate ngày hiện tại
     * @return List<Vehicle> danh sách xe hết bảo hành
     */
    @Query("SELECT v FROM Vehicle v WHERE v.warrantyEndDate < :currentDate")
    List<Vehicle> findVehiclesOutOfWarranty(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Tìm xe theo service center và trạng thái hoạt động
     * @param serviceCenterId ID của service center
     * @param isActive trạng thái hoạt động
     * @return List<Vehicle> danh sách xe thỏa mãn điều kiện
     */
    List<Vehicle> findByServiceCenterIdAndIsActive(Long serviceCenterId, Boolean isActive);
    
    /**
     * Custom query để tìm xe theo nhiều tiêu chí
     * @param brand thương hiệu
     * @param model model
     * @param serviceCenterId ID service center
     * @return List<Vehicle> danh sách xe thỏa mãn điều kiện
     */
    @Query("SELECT v FROM Vehicle v WHERE " +
           "(:brand IS NULL OR v.brand = :brand) AND " +
           "(:model IS NULL OR v.model = :model) AND " +
           "(:serviceCenterId IS NULL OR v.serviceCenterId = :serviceCenterId)")
    List<Vehicle> findVehiclesByCriteria(@Param("brand") String brand, 
                                        @Param("model") String model, 
                                        @Param("serviceCenterId") Long serviceCenterId);
}