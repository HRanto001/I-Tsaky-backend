CREATE TABLE clients if not exist(
                         id SERIAL PRIMARY KEY,
                         nom VARCHAR(150) NOT NULL,
                         type_client VARCHAR(50) CHECK (type_client IN ('Particulier', 'Epicerie', 'Restaurant', 'Touriste')),
                         telephone VARCHAR(20),
                         email VARCHAR(150),
                         adresse TEXT
);
