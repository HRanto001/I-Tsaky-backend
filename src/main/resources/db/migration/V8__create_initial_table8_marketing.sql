CREATE TABLE IF NOT EXISTS marketing (
                                         id BIGSERIAL PRIMARY KEY,
                                         canal VARCHAR(50),
    cout DOUBLE PRECISION,
    date_action DATE DEFAULT CURRENT_DATE,
    description TEXT
    );