-- V1.0.5: INSERT DATA ROLE_PERMISSIONS

-- ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'ADMIN'), p.id FROM permissions p;


-- USER
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE name = 'USER'), p.id FROM permissions p
WHERE p.name IN (
    -- USER MANAGEMENT
    'user:own:manage',

    -- BOARD (PROJECT)
    'board:create',

    -- Board member role
    'board:own:manage',
    'board:member:manage',
    'list:board:manage',
    'list:board:read',
    'task:board:create',
    'task:board:read',
    'task:assigned:update',
    'task:board:delete',
    'task:comment:manage'
);
