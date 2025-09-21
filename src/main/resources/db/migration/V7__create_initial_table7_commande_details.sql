CREATE TABLE IF NOT EXISTS commande_details (
                                                id BIGSERIAL PRIMARY KEY,
                                                commande_id BIGINT NOT NULL REFERENCES commandes(id) ON DELETE CASCADE,
    produit_id BIGINT NOT NULL REFERENCES produits(id),
    quantite INTEGER,
    prix_total DOUBLE PRECISION
    );