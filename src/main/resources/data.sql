-- Utilisateurs
INSERT INTO utilisateurs (nom, prenom, role, email, mot_de_passe)
VALUES
    ('Admin', 'Principal', 'ADMIN', 'admin@madachips.com', 'admin123'),
    ('Ranto', 'Handraina', 'PRODUCTION', 'ranto@madachips.com', '12345');

-- Produits
INSERT INTO produits ( nom, categorie, prix_unitaire, stock_disponible)
VALUES
    ( 'Chips Voanjo',  'CHIPS', 1200, 100),
    ( 'Chips Ovy', 'CHIPS', 1000, 150),
    ( 'Cacapigeon', 'SNACK', 1500, 50);

-- Clients
INSERT INTO clients ( nom, type_client, telephone, email, adresse)
VALUES
    ( 'Epicerie Tsena Soa', 'EPICERIE', '0321234567', 'epicerie@tsena.com', 'Antananarivo'),
    ( 'Jean Rakoto', 'PARTICULIER', '0349876543', 'jean@gmail.com', 'Ambohimanarina');

-- Commandes
INSERT INTO commandes ( client_id, date_commande, statut)
VALUES
    (1, NOW(), 'PAYEE'),
    (2, NOW(), 'EN_ATTENTE');

-- Détails Commande
INSERT INTO commande_details (commande_id, produit_id, quantite, prix_total)
VALUES
    ( 1, 1, 10, 12000),
    (1, 2, 5, 5000),
    (2, 3, 3, 4500);

-- Dépenses
INSERT INTO depenses ( type_depense, montant, date_depense, description)
VALUES
    ('MATIERE_PREMIERE', 50000, CURRENT_DATE, 'Achat ovy'),
    ('EMBALLAGE', 15000, CURRENT_DATE, 'Sachets plastiques');

-- Stocks
INSERT INTO stocks ( nom_matiere, quantite, unite, seuil_alerte)
VALUES
    ('Ovy', 100, 'KG', 20),
    ('Voanjo', 50, 'KG', 10);

-- Marketing
INSERT INTO marketing ( canal, cout, date_action, description)
VALUES
    ('FACEBOOK', 20000, CURRENT_DATE, 'Campagne Facebook Août 2025'),
    ('MARCHE_LOCAL', 10000, CURRENT_DATE, 'Participation foire artisanale');
