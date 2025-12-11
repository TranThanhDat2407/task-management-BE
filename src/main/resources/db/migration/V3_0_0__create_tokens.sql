-- V3.0.0: CREATE TABLE FOR STORING REFRESH TOKENS

CREATE TABLE tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    token_value TEXT NOT NULL UNIQUE,

    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,

    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    is_revoked BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Index  user_id to revoke  all token in 1 User
CREATE INDEX idx_rt_user_id ON tokens (user_id);

-- Index in expiry_date to clean tokens expired (cleanup job)
CREATE INDEX idx_rt_expiry_date ON tokens (expiry_date);