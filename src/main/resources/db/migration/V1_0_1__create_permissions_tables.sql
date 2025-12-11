-- V1.0.1: CREATE PERMISSIONS AND ROLE_PERMISSIONS

-- 1. TABLE permissions:
CREATE TABLE permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

-- 2. TABLE role_Permissions: RELATIONSHIP N:N BETWEEN roles AND permissions
CREATE TABLE role_permissions (
    role_id INTEGER NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id INTEGER NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,

    PRIMARY KEY (role_id, permission_id)
);

CREATE INDEX idx_rp_role_id ON role_permissions (role_id);