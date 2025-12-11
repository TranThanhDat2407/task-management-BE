-- V1.0.1: CREATE PERMISSIONS and ROLE_PERMISSIONS

-- 1. Bảng Permissions: Định nghĩa các quyền hạn chi tiết
CREATE TABLE permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL -- Ví dụ: task:create, user:delete, project:manage
);

-- 2. Bảng Role_Permissions: Liên kết N:N giữa Vai trò và Quyền hạn
-- Đây là bảng liên kết (Join Table) để một vai trò có thể có nhiều quyền, và một quyền có thể thuộc nhiều vai trò.
CREATE TABLE role_permissions (
    role_id INTEGER NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id INTEGER NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,

    PRIMARY KEY (role_id, permission_id)
);

-- Index để tăng tốc độ truy vấn kiểm tra quyền hạn (Role -> Permission)
CREATE INDEX idx_rp_role_id ON role_permissions (role_id);