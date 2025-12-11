-- V1.0.3: INSERT VALUE ROLES AND USER ADMIN DEFAULT

-- 1. INSERT VALUE roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'System Admin'),
('MANAGER', 'Project Manager'),
('USER', 'Application User');

-- 2. INSERT VALUE Users (Admin)
INSERT INTO users (email, username, password, role_id) VALUES
(
    'admin@admin.com',
    'admin',
    '$2a$12$qIkQqMU/x3Oq.Sgaag8ESOCDqoWQ13Nq/OkbA9Hn1IPPn.VSVQT8W', -- actual: Admin123
    ( SELECT id
      FROM roles
      WHERE name = 'ADMIN' )-- ID của Admin
);