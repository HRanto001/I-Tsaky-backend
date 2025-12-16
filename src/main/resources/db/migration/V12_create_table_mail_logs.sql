CREATE TABLE email_logs (
                            id BIGSERIAL PRIMARY KEY,
                            recipient VARCHAR(255) NOT NULL,
                            subject VARCHAR(255) NOT NULL,
                            body TEXT NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            sent_at TIMESTAMP
);