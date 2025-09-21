CREATE TABLE IF NOT EXISTS utilisateurs (
                                            id BIGSERIAL PRIMARY KEY,
                                            nom VARCHAR(255),
    prenom VARCHAR(255),
    role VARCHAR(50),
    email VARCHAR(255) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    actif BOOLEAN NOT NULL DEFAULT false
    );