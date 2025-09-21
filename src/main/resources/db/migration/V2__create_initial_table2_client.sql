CREATE TABLE IF NOT EXISTS clients (
                                       id BIGSERIAL PRIMARY KEY,
                                       nom VARCHAR(255),
    type_client VARCHAR(50),
    telephone VARCHAR(50),
    email VARCHAR(255),
    adresse VARCHAR(255)
    );