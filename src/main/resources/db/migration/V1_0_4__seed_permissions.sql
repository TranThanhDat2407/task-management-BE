-- V1.0.4: INSERT VALUE FOR PERMISSIONS

-- Resource:Scope:Action
-- Resource: The Entity (:user, role, project, list, task, comment)

-- Scope: Define the scope (
    -- all → Applies to all records in the system

    -- own → Applies only to items created/owned by the user

    -- assigned → Applies only to items assigned to the user (commonly used for tasks)

    -- board → Applies to all items within the same board that the user is part of
-- )

-- Action: :read, create, update, delete, manage ( includes create + read + update + delete )

INSERT INTO permissions (id, name) VALUES
-- Global Role: Admin, User
-- Board Member Role: Owner, Manager, Standard
-- USER MANAGEMENT
(1, 'user:all:manage'), -- CRUD  all the user (Global Role: Admin)
(2, 'user:own:manage'),   -- CRUD own profile (Global Role: User)

-- ROLE MANAGEMENT
(3, 'role:manage'),     -- CRUD all the role (Global Role: Admin)

-- BOARD
(4, 'board:all:read'),     -- Get all the board in system (Global Role: Admin)
(5, 'board:all:manage'),   -- CRUD all the board in system (Global Role: Admin)
(6, 'board:create'),       -- Create new board (Global Role: User)
(7, 'board:own:manage'),   -- CRUD own board (Global Role: User) (Board Member Role: Owner, Manager)
(8, 'board:member:manage'),-- CRUD member in board (Global Role: User) (Board Member Role: Owner, Manager)

-- LIST
(9, 'list:board:manage'), -- CRUD List in board (Global Role: User) (Board Member Role: Owner, Manager)
(10, 'list:board:read'),   -- Read only list board (Global Role: User) (Board Member Role: Standard)

-- TASK
(11, 'task:board:create'), -- CREATE Task. (Global Role: User ) (Member Scope: Owner, Manager, Standard)
(12, 'task:board:read'), -- Read all task in board (Global Role: User) (Member Scope: Owner, Manager, Standard)
(13, 'task:assigned:update'), -- Update Assigned task. (Global Role: User) (Member Scope: Owner, Manager, Standard)
(14, 'task:board:delete'), -- Delete task. (Global Role: User) (Member Scope: Owner, Manager)
(15, 'task:comment:manage'); -- CRUD Comment. (Global Role: User) (Member Scope: Owner, Manager, Standard)

SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions), TRUE);