# OEM EV Warranty Management System

Hệ thống quản lý bảo hành xe điện OEM được xây dựng bằng Spring Boot.

## Tính năng chính

### 1. Authentication & Authorization
- Đăng nhập/đăng xuất với JWT
- 4 loại user roles: Admin, EVM Staff, SC Staff, SC Technician
- Bảo mật API endpoints theo role

### 2. User Management
- CRUD operations cho User
- Quản lý roles và permissions
- Kích hoạt/vô hiệu hóa users

### 3. Vehicle Management
- CRUD operations cho Vehicle
- Quản lý thông tin xe theo VIN
- Theo dõi thời hạn bảo hành
- Quản lý thông tin khách hàng

### 4. Part Management
- CRUD operations cho Parts
- Quản lý tồn kho phụ tùng
- Theo dõi serial numbers
- Cảnh báo thiếu hụt phụ tùng

### 5. Warranty Claim Management
- Tạo và theo dõi warranty claims
- Workflow: Submitted → Pending → Approved → In Progress → Completed
- Phân công kỹ thuật viên
- Quản lý chi phí bảo hành

## Cấu trúc dự án

```
src/main/java/com/oem/ev/
├── EvWarrantyManagementApplication.java    # Main class
├── model/                                  # Entity classes
│   ├── User.java
│   ├── Vehicle.java
│   ├── Part.java
│   └── WarrantyClaim.java
├── repository/                             # Repository interfaces
│   ├── UserRepository.java
│   ├── VehicleRepository.java
│   ├── PartRepository.java
│   └── WarrantyClaimRepository.java
├── service/                                # Service classes
│   ├── UserService.java
│   └── VehicleService.java
├── controller/                             # REST Controllers
│   ├── AuthController.java
│   ├── UserController.java
│   └── VehicleController.java
└── security/                              # Security configuration
    ├── JwtUtil.java
    ├── JwtAuthenticationFilter.java
    ├── CustomUserDetailsService.java
    └── SecurityConfig.java
```

## Yêu cầu hệ thống

- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Spring Boot 3.2.0

## Cài đặt và chạy

### 1. Cài đặt MySQL
```bash
# Tạo database
CREATE DATABASE ev_warranty_db;
```

### 2. Cấu hình database
Chỉnh sửa file `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ev_warranty_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: your_username
    password: your_password
```

### 3. Chạy ứng dụng
```bash
# Build project
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: http://localhost:8080

## API Endpoints

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/logout` - Đăng xuất
- `POST /api/auth/register` - Đăng ký (Admin only)
- `GET /api/auth/me` - Lấy thông tin user hiện tại

### User Management
- `GET /api/users` - Lấy tất cả users (Admin only)
- `GET /api/users/{id}` - Lấy user theo ID
- `POST /api/users` - Tạo user mới (Admin only)
- `PUT /api/users/{id}` - Cập nhật user
- `DELETE /api/users/{id}` - Xóa user (Admin only)
- `PUT /api/users/{id}/activate` - Kích hoạt user (Admin only)
- `PUT /api/users/{id}/change-password` - Đổi password

### Vehicle Management
- `GET /api/vehicles` - Lấy tất cả vehicles
- `GET /api/vehicles/{id}` - Lấy vehicle theo ID
- `GET /api/vehicles/vin/{vin}` - Lấy vehicle theo VIN
- `GET /api/vehicles/service-center/{serviceCenterId}` - Lấy vehicles theo service center
- `GET /api/vehicles/customer/{customerName}` - Lấy vehicles theo tên khách hàng
- `GET /api/vehicles/under-warranty` - Lấy vehicles còn bảo hành
- `GET /api/vehicles/out-of-warranty` - Lấy vehicles hết bảo hành
- `GET /api/vehicles/search` - Tìm kiếm vehicles
- `POST /api/vehicles` - Tạo vehicle mới
- `PUT /api/vehicles/{id}` - Cập nhật vehicle
- `DELETE /api/vehicles/{id}` - Xóa vehicle
- `PUT /api/vehicles/{id}/activate` - Kích hoạt vehicle
- `PUT /api/vehicles/{id}/customer` - Cập nhật thông tin khách hàng

## User Roles và Permissions

### Admin
- Toàn quyền truy cập tất cả endpoints
- Quản lý users, vehicles, parts, warranty claims

### EVM Staff (Manufacturer Staff)
- Quản lý products và parts
- Phê duyệt warranty claims
- Tạo recall campaigns
- Xem báo cáo và thống kê

### SC Staff (Service Center Staff)
- Quản lý vehicles và customers
- Tạo warranty claims
- Cập nhật thông tin service

### SC Technician
- Xem và cập nhật warranty claims được phân công
- Cập nhật tiến độ sửa chữa

## Database Schema

### Users Table
- id, username, password, email, full_name
- role (ADMIN, EVM_STAFF, SC_STAFF, SC_TECHNICIAN)
- service_center_id, is_active
- created_at, updated_at

### Vehicles Table
- id, vin, model, brand, manufacture_date
- warranty_start_date, warranty_end_date
- customer_name, customer_phone, customer_email
- service_center_id, is_active
- created_at, updated_at

### Parts Table
- id, part_number, part_name, part_type
- serial_number, vehicle_vin
- warranty_period_months, unit_price
- stock_quantity, minimum_stock_level
- is_active, created_at, updated_at

### Warranty Claims Table
- id, claim_number, vehicle_id, vehicle_vin
- service_center_id, technician_id
- problem_description, diagnostic_report
- inspection_images, status
- estimated_cost, actual_cost, parts_required
- work_performed, completion_date
- created_by, approved_by, approved_at
- created_at, updated_at

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

## Deployment

### Production Configuration
1. Thay đổi JWT secret key
2. Cấu hình database production
3. Enable HTTPS
4. Cấu hình logging level

### Docker Deployment
```bash
# Build Docker image
docker build -t ev-warranty-management .

# Run container
docker run -p 8080:8080 ev-warranty-management
```

## Troubleshooting

### Common Issues
1. **Database Connection Error**: Kiểm tra cấu hình database trong application.yml
2. **JWT Token Error**: Kiểm tra JWT secret key
3. **CORS Error**: Kiểm tra CORS configuration trong SecurityConfig

### Logs
Xem logs tại: `logs/application.log`

## Contributing

1. Fork repository
2. Tạo feature branch
3. Commit changes
4. Push to branch
5. Tạo Pull Request

## License

MIT License