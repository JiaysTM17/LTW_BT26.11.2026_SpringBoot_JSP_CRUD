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
├── pom.xml                                 # Cấu hình Maven dependencies & plugins (Tomcat Jasper, SiteMesh 3.2.2)
├── database.sql                            # Kịch bản DDL tạo bảng và dữ liệu mẫu
├── README.md                               # Tài liệu hướng dẫn và báo cáo kỹ thuật
├── CHAY_UNG_DUNG.bat                       # Script khởi chạy ứng dụng nhanh cho Windows
├── src/
│   ├── main/
│   │   ├── java/vn/iotstar/
│   │   │   ├── SpringBootApplicationMain.java   # Khởi chạy ứng dụng, SiteMesh Filter bean, Seed data
│   │   │   ├── config/
│   │   │   │   ├── CustomSiteMeshFilter.java    # Bộ lọc Sitemesh 3 điều hướng decorator
│   │   │   │   ├── JSPStaticResourceConfigurer.java # Cấu hình nạp tài nguyên tĩnh JSP với Tomcat
│   │   │   │   ├── TomcatJSPConfiguration.java  # Tích hợp Tomcat Context Customizer cho JSP
│   │   │   │   └── WebMvcConfig.java            # Đăng ký Interceptor bảo mật & Resource Handlers
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java          # Xử lý Login, Register, Logout
│   │   │   │   ├── HomeController.java          # Trang chủ người dùng Portal
│   │   │   │   └── admin/
│   │   │   │       ├── AdminCategoryController.java # CRUD Category (/searchpaginated, /addOrEdit, /save, /delete)
│   │   │   │       └── AdminUserController.java     # CRUD User (/searchpaginated, /addOrEdit, /save, /delete)
│   │   │   ├── entity/
│   │   │   │   ├── Category.java                # Entity ánh xạ bảng categories (CategoryId, CategoryName)
│   │   │   │   └── User.java                    # Entity ánh xạ bảng users (id, username, password, roleid,...)
│   │   │   ├── interceptor/
│   │   │   │   └── AdminSecurityInterceptor.java# Kiểm tra phiên đăng nhập và quyền Admin
│   │   │   ├── repository/
│   │   │   │   ├── CategoryRepository.java      # JpaRepository (findByCategorynameContaining)
│   │   │   │   └── UserRepository.java          # JpaRepository (findByUsernameContaining Or Fullname Or Email)
│   │   │   ├── service/
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── UserService.java
│   │   │   │   └── impl/
│   │   │   │       ├── CategoryServiceImpl.java
│   │   │   │       └── UserServiceImpl.java
│   │   │   └── util/
│   │   │       └── PasswordUtil.java            # Tiện ích hash & verify mật khẩu BCrypt
│   │   ├── resources/
│   │   │   └── application.properties           # Cấu hình DataSource, JPA, Port 8090, JSP prefix/suffix
│   │   └── webapp/
│   │       ├── static/css/
│   │       │   └── style.css                    # CSS hệ thống thiết kế Clean Slate
│   │       ├── decorators/
│   │       │   ├── web.jsp                      # Layout Sitemesh cho phía Web Portal / Auth
│   │       │   └── admin.jsp                    # Layout Sitemesh cho trang Quản trị Admin
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
│   │               │   ├── list.jsp                 # Bảng danh mục có phân trang & chọn size (3, 5, 10, 15, 20)
│   │               │   └── form.jsp                 # Form thêm / chỉnh sửa danh mục
│   │               └── user/
│   │                   ├── list.jsp                 # Bảng người dùng có phân trang & chọn size
│   │                   └── form.jsp                 # Form thêm / chỉnh sửa người dùng
```

---

## 4. Hướng dẫn Khởi chạy Ứng dụng

### Cách 1: Khởi chạy nhanh bằng File Script (Khuyên dùng)
Nhấp đúp chuột vào file **`CHAY_UNG_DUNG.bat`** tại thư mục gốc hoặc thư mục `Source`. Hệ thống sẽ tự động khởi động ứng dụng với JDK 21 và chạy file JAR đã đóng gói sẵn.

### Cách 2: Khởi chạy qua dòng lệnh Maven
Mở terminal tại thư mục `Source` và thực thi:
```bash
mvn clean package -DskipTests
java -jar target/24110032_TruongGiaKiet_BT26.11.2026-1.0.0.jar
```

Ứng dụng khởi chạy tại cổng **8090** (theo tài liệu cấu hình của giảng viên):
* **URL Đăng nhập:** `http://localhost:8090/login`
* **URL Đăng ký:** `http://localhost:8090/register`
* **URL Trang chủ:** `http://localhost:8090/home`
* **URL Phân trang & Tìm kiếm Danh mục:** `http://localhost:8090/admin/categories/searchpaginated`
* **URL Phân trang & Tìm kiếm Người dùng:** `http://localhost:8090/admin/users/searchpaginated`

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
