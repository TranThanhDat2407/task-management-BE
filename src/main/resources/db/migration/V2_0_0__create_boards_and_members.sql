-- V2.0.0: CREATE TABLE BOARDS AND BOARD_MEMBERS

-- 1. Table boards
CREATE TABLE boards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    title VARCHAR(255) NOT NULL,
    description TEXT,

    status VARCHAR(50) DEFAULT 'Active' CHECK (status IN ('Active', 'On Hold', 'Completed', 'Archived')),

    start_date DATE NOT NULL,
    end_date DATE,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    updated_by UUID REFERENCES users(id) ON DELETE SET NULL
)

-- Index created_by to quick query board by 1 user (owner)
CREATE INDEX idx_boards_creator ON boards (created_by);

-- 2.  Board_Members: Liên kết N:N between users and board
CREATE TABLE board_members (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    board_id UUID NOT NULL REFERENCES boards(id) ON DELETE CASCADE,

    member_role VARCHAR(50) NOT NULL CHECK (member_role IN ('Owner', 'Manager', 'Standard')),

    PRIMARY KEY (user_id, board_id),

    assigned_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index to quick query all boards by  user
CREATE INDEX idx_board_members_user ON board_members (user_id);