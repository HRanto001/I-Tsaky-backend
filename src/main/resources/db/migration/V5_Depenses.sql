CREATE TABLE depenses if not exist(
                          id SERIAL PRIMARY KEY,
                          type_depense VARCHAR(100) CHECK (type_depense IN ('Matière première', 'Emballage', 'Transport', 'Marketing', 'Autre')),
                          montant NUMERIC(12,2) NOT NULL,
                          date_depense DATE DEFAULT CURRENT_DATE,
                          description TEXT
);
