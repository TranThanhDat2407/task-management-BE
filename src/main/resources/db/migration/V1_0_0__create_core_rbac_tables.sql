-- V1.0.0: TẠO BẢNG ROLES VÀ USERS CƠ BẢN

-- 1. Kích hoạt tiện ích mở rộng UUID (Nếu chưa có)
-- Spring Boot/Flyway sẽ chỉ chạy lệnh này nếu nó chưa được tạo
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. Bảng Roles: Định nghĩa các vai trò trong hệ thống
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL CHECK (name IN ('Admin', 'Manager', 'User', 'Guest')),
    description TEXT
);

-- 3. Bảng Users: Lưu trữ thông tin người dùng
-- (role_id sẽ được thêm sau trong V1.0.2 để quản lý thứ tự dependency với V2.0.0 - Seed Data)
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Sử dụng UUID làm PK

    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL, -- Lưu trữ hash mật khẩu

    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP WITHOUT TIME ZONE,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index trên email để tăng tốc độ tìm kiếm khi đăng nhập
CREATE UNIQUE INDEX idx_user_email_unique ON users (email);