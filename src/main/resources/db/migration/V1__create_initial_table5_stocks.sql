CREATE TABLE IF NOT EXISTS stocks (
                                      id BIGSERIAL PRIMARY KEY,
                                      nom_matiere VARCHAR(255),
    quantite INTEGER,
    unite VARCHAR(50),
    seuil_alerte INTEGER
    );