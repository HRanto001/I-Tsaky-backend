CREATE TABLE IF NOT EXISTS activation_keys (
                                               id BIGSERIAL PRIMARY KEY,
                                               key_value VARCHAR(255) UNIQUE NOT NULL,
    used BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP,
    expires_at TIMESTAMP
    );