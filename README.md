# 🛍️ Ecom - Nền Tảng Thương Mại Điện Tử & Quản Lý Bán Hàng Thời Trang

[![Java](https://img.shields.io/badge/Java-17-orange.svg?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-green.svg?logo=springsecurity)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-0.12.5-black.svg?logo=jsonwebtokens)](https://jwt.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg?logo=mysql)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Dự án website thương mại điện tử chuyên ngành thời trang với kiến trúc RESTful API Backend trên nền tảng **Spring Boot 3** kết hợp giao diện Web hiện đại, hỗ trợ đầy đủ quy trình mua sắm của khách hàng và bảng điều khiển quản trị (Admin Dashboard) toàn diện.

---

## 📑 Mục lục
- [Giới thiệu](#-giới-thiệu)
- [Tính năng nổi bật](#-tính-năng-nổi-bật)
- [Kiến trúc & Công nghệ](#-kiến-trúc--công-nghệ)
- [Cấu trúc thư mục](#-cấu-trúc-thư-mục)
- [Yêu cầu hệ thống](#-yêu-cầu-hệ-thống)
- [Hướng dẫn cài đặt & Khởi chạy](#-hướng-dẫn-cài-đặt--khởi-chạy)
- [Tài khoản mặc định](#-tài-khoản-mặc-định)
- [Tài liệu API (REST Endpoints)](#-tài-liệu-api-rest-endpoints)
- [Hướng dẫn đẩy code lên GitHub](#-hướng-dẫn-đẩy-code-lên-github)
- [Đóng góp & Giấy phép](#-đóng-góp--giấy-phép)

---

## 🌟 Giới thiệu

**Ecom** cung cấp giải pháp trọn gói cho một hệ thống bán lẻ thời trang trực tuyến:
- **Người mua (User):** Trải nghiệm xem sản phẩm, phân loại theo danh mục, tìm kiếm, giỏ hàng trực quan, đặt hàng nhanh chóng và theo dõi lịch sử đơn hàng.
- **Quản trị viên (Admin):** Thống kê doanh thu thời gian thực, quản lý kho sản phẩm (thêm/sửa/xóa kèm upload ảnh), quản trị danh mục và quy trình xử lý đơn hàng chuyên nghiệp.

---

## ✨ Tính năng nổi bật

### 1. Phân hệ Khách hàng (Client / User)
* **Xác thực an toàn:** Đăng ký tài khoản, đăng nhập với JWT Token, tự động lưu phiên làm việc.
* **Duyệt & Tìm kiếm:** Lọc sản phẩm theo danh mục thời trang (Áo, Quần & Jeans, Váy & Đầm, v.v.), tìm kiếm tức thì theo từ khóa.
* **Chi tiết sản phẩm:** Xem hình ảnh thực tế, mô tả chi tiết, giá bán, số lượng tồn kho.
* **Giỏ hàng & Đặt hàng:**
  * Thêm/bớt/xóa sản phẩm, cập nhật số lượng tức thời.
  * Điền thông tin giao hàng (họ tên, số điện thoại, địa chỉ, ghi chú).
  * Đặt hàng và nhận mã đơn hàng tự động.
* **Quản lý đơn cá nhân:** Xem danh sách đơn hàng đã mua, theo dõi trạng thái đơn và chi tiết từng mặt hàng.

### 2. Phân hệ Quản trị (Admin Dashboard)
* **Thống kê tổng quan (Dashboard):**
  * Tổng doanh thu, tổng số đơn đặt, tổng số sản phẩm và số đơn đang chờ xử lý.
* **Quản lý sản phẩm (Product CRUD):**
  * Thêm mới, chỉnh sửa thông tin, giá, danh mục, tồn kho.
  * Tích hợp upload ảnh trực tiếp lên server và gắn link tự động.
* **Quản lý danh mục (Category CRUD):** Phân loại cây danh mục linh hoạt.
* **Quản lý đơn hàng (Order Management):**
  * Danh sách toàn bộ đơn đặt hàng trong hệ thống.
  * Cập nhật trạng thái: `PENDING` ➔ `PROCESSING` ➔ `SHIPPED` ➔ `DELIVERED` ➔ `CANCELLED`.
  * Cập nhật mã vận đơn (Tracking Number) phục vụ theo dõi vận chuyển.

### 3. Tự động hóa hệ thống (Data Seeding)
* Tự động khởi tạo quyền `ROLE_USER` và `ROLE_ADMIN` khi khởi động.
* Tự động tạo tài khoản Admin mặc định với mật khẩu mã hóa BCrypt.
* Tự động khởi tạo các danh mục thời trang mẫu nếu database còn trống.

---

## 🛠️ Kiến trúc & Công nghệ

### Backend
* **Ngôn ngữ:** Java 17
* **Framework:** Spring Boot 3.2.5
* **Bảo mật:** Spring Security 6, JWT (io.jsonwebtoken 0.12.5), BCrypt
* **Cơ sở dữ liệu & ORM:** MySQL 8, Spring Data JPA, Hibernate 6, HikariCP
* **Validation:** Hibernate Validator (Jakarta Validation)
* **Tiện ích:** Lombok, SLF4J / Logback

### Frontend
* **Giao diện:** HTML5, CSS3, JavaScript (Vanilla JS thuần, tối ưu tốc độ và không phụ thuộc thư viện nặng)
* **Giao tiếp API:** Fetch API, JWT Bearer Token Headers
* **Giao diện:** Trang người dùng (`/index.html`) & Trang quản trị (`/admin.html`)

---

## 📂 Cấu trúc thư mục

```text
Ecom/
├── .mvn/                             # Maven wrapper config
├── src/
│   ├── main/
│   │   ├── java/ShopApi/Ecom/
│   │   │   ├── config/               # Cấu hình CORS, DataInitializer, UploadResourceConfig
│   │   │   ├── controller/           # REST Controllers (Auth, Product, Category, Order, Admin, User)
│   │   │   ├── dto/                  # Data Transfer Objects (Request/Response)
│   │   │   ├── entity/               # JPA Entities (User, Role, Product, Category, Order, OrderItem)
│   │   │   ├── exception/            # Global Exception Handler & Custom Exceptions
│   │   │   ├── repository/           # Spring Data JPA Repositories
│   │   │   ├── security/             # SecurityConfig, JwtAuthFilter, JwtService
│   │   │   ├── service/              # Business Logic Services & Storage Service
│   │   │   └── EcomApplication.java  # Main Application Entry Point
│   │   └── resources/
│   │       ├── static/               # Giao diện Web (index.html, admin.html, styles, scripts)
│   │       └── application.properties# Cấu hình Database, JWT, Port, Upload
│   └── test/                         # Unit & Integration Tests
├── uploads/                          # Thư mục chứa ảnh sản phẩm tải lên
├── mvnw / mvnw.cmd                   # Maven Wrapper scripts
├── pom.xml                           # Cấu hình dependencies Maven
└── README.md                         # Tài liệu dự án
```

---

## 💻 Yêu cầu hệ thống

Trước khi chạy dự án, máy tính cần cài đặt sẵn:
- **Java Development Kit (JDK):** Phiên bản **17** trở lên (`java -version`)
- **MySQL Server:** Phiên bản **8.0+**
- **Git** (nếu muốn clone và đẩy code)

---

## 🚀 Hướng dẫn cài đặt & Khởi chạy

### Bước 1: Chuẩn bị Cơ sở dữ liệu MySQL

1. Mở MySQL Workbench hoặc dòng lệnh MySQL:
```sql
CREATE DATABASE shop_api CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Kiểm tra cấu hình trong file [application.properties](src/main/resources/application.properties):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shop_api?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=123456
server.port=8080
```
> *(Lưu ý thay đổi username/password nếu tài khoản MySQL của bạn khác với thông số trên)*

---

### Bước 2: Chạy ứng dụng

#### Trên Windows (PowerShell / CMD):
```powershell
.\mvnw.cmd spring-boot:run
```

#### Trên Linux / macOS:
```bash
chmod +x ./mvnw
./mvnw spring-boot:run
```

Khi màn hình xuất hiện thông báo:
```text
Tomcat started on port 8080 (http) with context path ''
Started EcomApplication in ... seconds
```
Ứng dụng đã khởi chạy thành công!

---

### Bước 3: Truy cập hệ thống

| Phân hệ | Đường dẫn truy cập | Mô tả |
| :--- | :--- | :--- |
| **Trang mua sắm khách hàng** | [http://localhost:8080](http://localhost:8080) | Xem hàng, tìm kiếm, giỏ hàng, đặt hàng |
| **Trang Quản trị (Admin)** | [http://localhost:8080/admin.html](http://localhost:8080/admin.html) | Quản lý sản phẩm, đơn hàng, danh mục |

---

## 🔑 Tài khoản mặc định

Hệ thống tự động tạo sẵn tài khoản quản trị khi khởi chạy lần đầu:

| Vai trò | Tên đăng nhập | Mật khẩu | Quyền hạn |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` | Quản trị toàn quyền (`ROLE_ADMIN`, `ROLE_USER`) |
| **Khách hàng** | *(Tự đăng ký)* | *(Tự chọn)* | Mua hàng, xem đơn cá nhân (`ROLE_USER`) |

---

## 📡 Tài liệu API (REST Endpoints)

### 1. Xác thực (`/api/auth`)
| Phương thức | Endpoint | Yêu cầu quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public | Đăng ký tài khoản người dùng mới |
| `POST` | `/api/auth/login` | Public | Đăng nhập hệ thống, nhận JWT Token |

### 2. Sản phẩm & Ảnh (`/api/products`)
| Phương thức | Endpoint | Yêu cầu quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/products` | Public | Lấy danh sách sản phẩm (hỗ trợ lọc theo `categoryId`, `keyword`) |
| `GET` | `/api/products/{id}` | Public | Lấy chi tiết 1 sản phẩm |
| `POST` | `/api/products/images` | `ROLE_ADMIN` | Upload ảnh sản phẩm (`multipart/form-data`) |
| `POST` | `/api/products` | `ROLE_ADMIN` | Thêm mới sản phẩm |
| `PUT` | `/api/products/{id}` | `ROLE_ADMIN` | Cập nhật thông tin sản phẩm |
| `DELETE` | `/api/products/{id}` | `ROLE_ADMIN` | Xóa sản phẩm theo ID |

### 3. Danh mục (`/api/categories`)
| Phương thức | Endpoint | Yêu cầu quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/categories` | Public | Lấy tất cả danh mục hàng hóa |
| `GET` | `/api/categories/{id}` | Public | Lấy chi tiết 1 danh mục |
| `POST` | `/api/categories` | `ROLE_ADMIN` | Tạo danh mục mới |
| `PUT` | `/api/categories/{id}` | `ROLE_ADMIN` | Cập nhật danh mục |
| `DELETE` | `/api/categories/{id}` | `ROLE_ADMIN` | Xóa danh mục |

### 4. Đơn hàng khách hàng (`/api/orders`)
| Phương thức | Endpoint | Yêu cầu quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/orders` | Đã đăng nhập | Đặt đơn hàng mới kèm danh sách mặt hàng |
| `GET` | `/api/orders` | Đã đăng nhập | Xem danh sách đơn hàng của người dùng hiện tại |
| `GET` | `/api/orders/{id}` | Đã đăng nhập | Xem chi tiết 1 đơn hàng của người dùng |

### 5. Quản trị hệ thống (`/api/admin`)
| Phương thức | Endpoint | Yêu cầu quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/dashboard` | `ROLE_ADMIN` | Lấy dữ liệu thống kê doanh thu, số đơn, sản phẩm |
| `GET` | `/api/admin/orders` | `ROLE_ADMIN` | Xem toàn bộ danh sách đơn đặt hàng trong hệ thống |
| `GET` | `/api/admin/orders/{id}` | `ROLE_ADMIN` | Xem chi tiết đơn hàng bất kỳ |
| `PUT` | `/api/admin/orders/{id}/status` | `ROLE_ADMIN` | Cập nhật trạng thái đơn hàng & mã vận đơn |

### 6. Người dùng (`/api/users`)
| Phương thức | Endpoint | Yêu cầu quyền | Mô tả |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users/me` | Đã đăng nhập | Lấy thông tin cá nhân và vai trò hiện tại |

---

