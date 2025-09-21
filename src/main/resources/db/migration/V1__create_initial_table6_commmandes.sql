CREATE TABLE IF NOT EXISTS commandes (
                                         id BIGSERIAL PRIMARY KEY,
                                         client_id BIGINT NOT NULL REFERENCES clients(id) ON DELETE CASCADE,
    date_commande TIMESTAMP,
    statut VARCHAR(50),
    created_by BIGINT REFERENCES utilisateurs(id),
    updated_by BIGINT REFERENCES utilisateurs(id)
    );