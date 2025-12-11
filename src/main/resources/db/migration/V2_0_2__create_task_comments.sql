-- V2.0.2: CREATE STRUCTURE FOR TASK COMMENTS

CREATE TABLE task_comments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,

    content TEXT NOT NULL,

    created_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    updated_by UUID REFERENCES users(id) ON DELETE SET NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index  task_id get all comment in Task
CREATE INDEX idx_task_comments_task ON task_comments (task_id);

-- Index created_by to get all comment in User
CREATE INDEX idx_task_comments_creator ON task_comments (created_by);