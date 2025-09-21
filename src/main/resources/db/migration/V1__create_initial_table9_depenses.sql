CREATE TABLE IF NOT EXISTS depenses (
                                        id BIGSERIAL PRIMARY KEY,
                                        type_depense VARCHAR(50),
    montant DOUBLE PRECISION,
    date_depense DATE DEFAULT CURRENT_DATE,
    description TEXT,
    created_by BIGINT REFERENCES utilisateurs(id),
    updated_by BIGINT REFERENCES utilisateurs(id),
    updated_at TIMESTAMP
    );