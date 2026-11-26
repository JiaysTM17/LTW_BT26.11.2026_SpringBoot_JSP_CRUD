-- ==============================================================================
-- BÀI TẬP THỰC HÀNH LẬP TRÌNH WEB (26/11/2026) - SPRING BOOT 3 & JSP/JSTL CRUD
-- TRƯỜNG ĐẠI HỌC SƯ PHẠM KỸ THUẬT THÀNH PHỐ HỒ CHÍ MINH (HCMUTE)
-- KHOA CÔNG NGHỆ THÔNG TIN
-- Sinh viên: Trương Gia Kiệt - MSSV: 24110032 - Lớp: 24110CTNB
-- Đề bài: Auth (Login/Register/Logout) + Admin CRUD Category & User (Tìm kiếm, Phân trang)
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS servletjpa CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE servletjpa;

SET FOREIGN_KEY_CHECKS = 0;

-- 1. BẢNG USERS (Tài khoản, Phân quyền Role Admin/User, Mã hóa mật khẩu BCrypt)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    fullname NVARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(500),
    roleid INT DEFAULT 1,     -- 1: User, 2: Admin
    status INT DEFAULT 1,     -- 1: Hoạt động, 0: Bị khóa
    code VARCHAR(10),
    createddate DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. BẢNG CATEGORIES (Danh mục bài học / sản phẩm)
CREATE TABLE IF NOT EXISTS categories (
    CategoryId INT AUTO_INCREMENT PRIMARY KEY,
    CategoryName NVARCHAR(100) NOT NULL,
    Images NVARCHAR(500),
    Status INT DEFAULT 1      -- 1: Hoạt động, 0: Tạm ẩn
);

-- 3. SEED DỮ LIỆU MẪU
-- Mật khẩu mặc định: '123456' được mã hóa bằng BCrypt: $2a$10$EIXzaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW
INSERT INTO users (username, password, email, fullname, phone, avatar, roleid, status, createddate)
VALUES 
('admin', '$2a$10$EIXzaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'admin@iotstar.vn', N'Quản Trị Viên', '0909123456', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200', 2, 1, NOW()),
('kiet24110032', '$2a$10$EIXzaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', 'kiet24110032@student.hcmute.edu.vn', N'Trương Gia Kiệt', '0987654321', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200', 1, 1, NOW())
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO categories (CategoryId, CategoryName, Images, Status)
VALUES 
(1, N'Lập Trình Web Spring Boot', 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600', 1),
(2, N'Công Nghệ Phần Mềm & DevOps', 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600', 1),
(3, N'Kiến Trúc Microservices', 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600', 1),
(4, N'Cơ Sở Dữ Liệu & Tối Ưu Hóa', 'https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=600', 1),
(5, N'Trí Tuệ Nhân Tạo Ứng Dụng', 'https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=600', 1),
(6, N'Bảo Mật Web & Xác Thực', 'https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=600', 1)
ON DUPLICATE KEY UPDATE CategoryId=CategoryId;

SET FOREIGN_KEY_CHECKS = 1;
