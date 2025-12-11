-- V2.0.1: CREATE STRUCTURE FOR LISTS, TASKS, AND TASK ASSIGNMENTS

-- 1. Lists Table
CREATE TABLE lists (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    board_id UUID NOT NULL REFERENCES boards(id) ON DELETE CASCADE,

    title VARCHAR(100) NOT NULL,
    order_index INTEGER NOT NULL,
    is_archived BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    updated_by UUID REFERENCES users(id) ON DELETE SET NULL,

    -- Constraint: List title must be unique within a Board
    CONSTRAINT uq_board_list_title UNIQUE (board_id, title),

    -- Constraint: Display order must be unique within a Board
    CONSTRAINT uq_board_list_order UNIQUE (board_id, order_index)
);

CREATE INDEX idx_lists_board ON lists (board_id);


-- 2. Tasks Table
CREATE TABLE tasks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    list_id UUID NOT NULL REFERENCES lists(id) ON DELETE RESTRICT,
    board_id UUID NOT NULL REFERENCES boards(id) ON DELETE CASCADE,

    title VARCHAR(255) NOT NULL,
    description TEXT,

    priority VARCHAR(50) DEFAULT 'Medium' CHECK (priority IN ('High', 'Medium', 'Low')),
    due_date TIMESTAMP WITHOUT TIME ZONE,
    completed_at TIMESTAMP WITHOUT TIME ZONE,

    order_index INTEGER NOT NULL,


    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    updated_by UUID REFERENCES users(id) ON DELETE SET NULL,

    -- Constraint: Ensure the Board ID exists
    CONSTRAINT fk_task_board FOREIGN KEY (board_id) REFERENCES boards(id),


    -- Constraint: Task order must be unique within a List
    CONSTRAINT uq_list_task_order UNIQUE (list_id, order_index)
);

CREATE INDEX idx_tasks_list ON tasks (list_id);


-- 3. Task_Assignments Table
CREATE TABLE task_assignments (
    task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,

    PRIMARY KEY (task_id, user_id),

    assigned_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_assignments_user ON task_assignments (user_id);