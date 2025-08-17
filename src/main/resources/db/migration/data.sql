-- Utilisateurs
INSERT INTO utilisateurs (id, nom, prenom, role, email, mot_de_passe)
VALUES
    (1, 'Admin', 'Principal', 'ADMIN', 'admin@madachips.com', 'admin123'),
    (2, 'Ranto', 'Handraina', 'PRODUCTION', 'ranto@madachips.com', '12345');

-- Produits
INSERT INTO produits (id, nom, description, categorie, prix_unitaire, stock_disponible)
VALUES
    (1, 'Chips Voanjo', 'Chips artisanales à base de voanjo', 'CHIPS', 1200, 100),
    (2, 'Chips Ovy', 'Chips artisanales de pomme de terre locale', 'CHIPS', 1000, 150),
    (3, 'Cacapigeon', 'Snack typique malagasy', 'SNACK', 1500, 50);

-- Clients
INSERT INTO clients (id, nom, type_client, telephone, email, adresse)
VALUES
    (1, 'Epicerie Tsena Soa', 'EPICERIE', '0321234567', 'epicerie@tsena.com', 'Antananarivo'),
    (2, 'Jean Rakoto', 'PARTICULIER', '0349876543', 'jean@gmail.com', 'Ambohimanarina');

-- Commandes
INSERT INTO commandes (id, client_id, date_commande, statut)
VALUES
    (1, 1, NOW(), 'PAYEE'),
    (2, 2, NOW(), 'EN_ATTENTE');

-- Détails Commande
INSERT INTO commande_details (id, commande_id, produit_id, quantite, prix_total)
VALUES
    (1, 1, 1, 10, 12000),
    (2, 1, 2, 5, 5000),
    (3, 2, 3, 3, 4500);

-- Dépenses
INSERT INTO depenses (id, type_depense, montant, date_depense, description)
VALUES
    (1, 'MATIERE_PREMIERE', 50000, CURRENT_DATE, 'Achat ovy'),
    (2, 'EMBALLAGE', 15000, CURRENT_DATE, 'Sachets plastiques');

-- Stocks
INSERT INTO stocks (id, nom_matiere, quantite, unite, seuil_alerte)
VALUES
    (1, 'Ovy', 100, 'KG', 20),
    (2, 'Voanjo', 50, 'KG', 10);

-- Marketing
INSERT INTO marketing (id, canal, cout, date_action, description)
VALUES
    (1, 'FACEBOOK', 20000, CURRENT_DATE, 'Campagne Facebook Août 2025'),
    (2, 'MARCHE_LOCAL', 10000, CURRENT_DATE, 'Participation foire artisanale');
