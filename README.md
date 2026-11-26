# BÀI TẬP THỰC HÀNH LẬP TRÌNH WEB (26/11/2026)
## SPRING BOOT & JSP/JSTL - QUẢN LÝ USER & CATEGORY (CRUD, PHÂN TRANG, TÌM KIẾM)

* **Trường:** Đại học Sư phạm Kỹ thuật TP. Hồ Chí Minh (HCMUTE)
* **Khoa:** Công nghệ Thông tin
* **Sinh viên thực hiện:** Trương Gia Kiệt
* **Mã số sinh viên (MSSV):** 24110032
* **Lớp:** 24110CTNB
* **Ngày thực hành:** 26/11/2026

---

## 1. Mục tiêu và Yêu cầu Đề bài

Dự án được triển khai nhằm đáp ứng đầy đủ các yêu cầu theo đề bài:
1. **Chức năng Xác thực người dùng (Auth):**
   - Đăng ký tài khoản (`/register`): Kiểm tra trùng lặp `username`, `email`, kiểm tra khớp mật khẩu xác nhận, mã hóa mật khẩu an toàn bằng thuật toán **BCrypt Hash**.
   - Đăng nhập (`/login`): Xác thực tài khoản với mật khẩu BCrypt, kiểm tra trạng thái kích hoạt, lưu phiên làm việc (`HttpSession`). Tự động điều hướng: Admin vào trang Quản trị, User vào Trang chủ Portal.
   - Đăng xuất (`/logout`): Hủy `HttpSession`, xóa trạng thái đăng nhập và quay về màn hình đăng nhập kèm thông báo.
   - Bảo mật phân quyền (`AdminSecurityInterceptor`): Chặn toàn bộ các URL `/admin/**` nếu chưa đăng nhập hoặc không có quyền Admin (`roleid != 2`).

2. **Chức năng Quản trị (Admin CRUD với Phân trang & Tìm kiếm):**
   - **Bảng Category (Danh mục):**
     - Xem danh sách phân trang (Pageable: 5 bản ghi/trang, hỗ trợ phân trang Trước/Sau/Số trang).
     - Tìm kiếm linh hoạt theo tên danh mục (`keyword`).
     - Thêm mới danh mục có xác thực dữ liệu (`@Valid`).
     - Chỉnh sửa thông tin danh mục, cập nhật trạng thái hoạt động (Hoạt động / Tạm ẩn).
     - Xóa danh mục kèm hộp thoại xác nhận an toàn.
   - **Bảng User (Người dùng):**
     - Xem danh sách phân trang người dùng (5 bản ghi/trang).
     - Tìm kiếm đa năng theo `username`, `fullname` hoặc `email`.
     - Thêm mới tài khoản với phân quyền (`Admin` / `User`) và trạng thái (`Hoạt động` / `Khóa`).
     - Chỉnh sửa thông tin, đổi quyền hạn, trạng thái hoặc cập nhật mật khẩu mới.
     - Xóa tài khoản (bảo vệ chống tự xóa tài khoản đang đăng nhập).

3. **Giao diện & Trải nghiệm (Clean UI Design System):**
   - Tuân thủ thiết kế tối giản, chuyên nghiệp theo phong cách Engineering Craft:
   - Sử dụng thang màu trung tính (Slate/Graphite), không sử dụng hiệu ứng màu dải gradient tím/neon hay đổ bóng giả lập AI.
   - Loại bỏ hoàn toàn emoji trang trí trên nhãn nút bấm; thay thế bằng bộ icon đường nét tiêu chuẩn (SVG Stroke).
   - Hệ thống lưới khoảng cách chuẩn 4px, tỷ lệ lồng bo góc nhất quán.
   - Hiển thị số liệu và mã định danh theo chuẩn `tabular-nums`.

---

## 2. Công nghệ sử dụng

* **Ngôn ngữ:** Java 21 (LTS)
* **Framework:** Spring Boot 3.3.5
* **View Engine:** JSP (JavaServer Pages) & JSTL 3.0 (Jakarta EE 10)
* **Cơ sở dữ liệu:** MySQL 8.0 (Database: `servletjpa`)
* **ORM & Data Access:** Spring Data JPA / Hibernate Core 6.x
* **Bảo mật mật khẩu:** jBCrypt 0.4 (Blowfish crypt)
* **Build tool:** Apache Maven 3.10.0

---

## 3. Cấu trúc Thư mục Dự án

```text
Source/
├── pom.xml                                 # Cấu hình Maven dependencies & plugins
├── database.sql                            # Kịch bản DDL tạo bảng và dữ liệu mẫu
├── README.md                               # Tài liệu hướng dẫn và báo cáo kỹ thuật
├── src/
│   ├── main/
│   │   ├── java/vn/iotstar/
│   │   │   ├── SpringBootApplicationMain.java   # Lớp khởi chạy ứng dụng & seed dữ liệu
│   │   │   ├── config/
│   │   │   │   └── WebMvcConfig.java            # Cấu hình Interceptor & Static Resources
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java          # Xử lý Login, Register, Logout
│   │   │   │   ├── HomeController.java          # Trang chủ người dùng Portal
│   │   │   │   └── admin/
│   │   │   │       ├── AdminCategoryController.java # CRUD Category (Tìm kiếm, Phân trang)
│   │   │   │       └── AdminUserController.java     # CRUD User (Tìm kiếm, Phân trang)
│   │   │   ├── entity/
│   │   │   │   ├── Category.java                # Entity ánh xạ bảng categories
│   │   │   │   └── User.java                    # Entity ánh xạ bảng users
│   │   │   ├── interceptor/
│   │   │   │   └── AdminSecurityInterceptor.java# Kiểm tra phiên đăng nhập và quyền Admin
│   │   │   ├── repository/
│   │   │   │   ├── CategoryRepository.java      # JpaRepository cho Category
│   │   │   │   └── UserRepository.java          # JpaRepository cho User
│   │   │   ├── service/
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── impl/
│   │   │   │       ├── CategoryServiceImpl.java
│   │   │   │       └── UserServiceImpl.java
│   │   │   └── util/
│   │   │       └── PasswordUtil.java            # Tiện ích hash & verify mật khẩu BCrypt
│   │   ├── resources/
│   │   │   └── application.properties           # Cấu hình DataSource, JPA, Port, JSP
│   │   └── webapp/
│   │       ├── static/css/
│   │       │   └── style.css                    # CSS hệ thống thiết kế Clean Slate
│   │       └── WEB-INF/views/
│   │           ├── common/
│   │           │   ├── header.jsp
│   │           │   ├── sidebar.jsp
│   │           │   └── footer.jsp
│   │           ├── auth/
│   │           │   ├── login.jsp
│   │           │   └── register.jsp
│   │           ├── home/
│   │           │   └── index.jsp
│   │           └── admin/
│   │               ├── category/
│   │               │   ├── list.jsp
│   │               │   └── form.jsp
│   │               └── user/
│   │                   ├── list.jsp
│   │                   └── form.jsp
```

---

## 4. Hướng dẫn Khởi chạy Ứng dụng

### Bước 1: Khởi tạo Cơ sở dữ liệu MySQL
1. Khởi động dịch vụ MySQL Server (cổng 3306).
2. Chạy nội dung trong file `database.sql` trên công cụ quản trị (MySQL Workbench / phpMyAdmin / DBeaver) hoặc để Hibernate tự động sinh cấu trúc bảng theo thuộc tính `spring.jpa.hibernate.ddl-auto=update`.

### Bước 2: Build & Chạy Spring Boot
Mở terminal tại thư mục `Source` và thực thi:
```bash
mvn clean compile
mvn spring-boot:run
```

Ứng dụng sẽ khởi chạy tại cổng **8088**:
* **URL Đăng nhập:** `http://localhost:8088/login`
* **URL Trang chủ:** `http://localhost:8088/home`
* **URL Quản trị Danh mục:** `http://localhost:8088/admin/categories`
* **URL Quản trị Người dùng:** `http://localhost:8088/admin/users`

### Bước 3: Tài khoản thử nghiệm
Hệ thống tích hợp sẵn bộ nạp dữ liệu tự động (`CommandLineRunner`) với 2 tài khoản mẫu:
* **Tài khoản Quản trị (Admin):**
  * Tên đăng nhập: `admin`
  * Mật khẩu: `123456`
  * Quyền: `roleid = 2` (Toàn quyền truy cập bảng điều khiển Admin)
* **Tài khoản Người dùng thường (User):**
  * Tên đăng nhập: `kiet24110032`
  * Mật khẩu: `123456`
  * Quyền: `roleid = 1` (Chỉ truy cập cổng thông tin, bị chặn khi vào `/admin/**`)
