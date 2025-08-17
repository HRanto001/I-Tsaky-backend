CREATE TABLE commandes if not exist(
                           id SERIAL PRIMARY KEY,
                           client_id INT REFERENCES clients(id),
                           date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           statut VARCHAR(50) CHECK (statut IN ('En attente', 'Payée', 'Livrée', 'Annulée'))
);

CREATE TABLE commande_details (
                                  id SERIAL PRIMARY KEY,
                                  commande_id INT REFERENCES commandes(id) ON DELETE CASCADE,
                                  produit_id INT REFERENCES produits(id),
                                  quantite INT NOT NULL,
                                  prix_total NUMERIC(12,2) NOT NULL
);
