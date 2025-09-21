CREATE TABLE IF NOT EXISTS produits (
                                        id BIGSERIAL PRIMARY KEY,
                                        nom VARCHAR(255),
    prix_unitaire DOUBLE PRECISION,
    categorie VARCHAR(50),
    stock_disponible INTEGER
    );