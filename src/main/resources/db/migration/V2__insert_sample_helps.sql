-- Insérer des donateurs
INSERT INTO donor (name, email) VALUES
                                    ('Jean Dupont', 'jean.dupont@example.com'),
                                    ('Marie Curie', 'marie.curie@example.com'),
                                    ('Paul Martin', 'paul.martin@example.com');

-- Insérer des bénéficiaires
INSERT INTO beneficiary (name, email) VALUES
                                          ('Alice Durand', 'alice.durand@example.com'),
                                          ('Bob Leroy', 'bob.leroy@example.com');

-- Insérer des paiements pour dons
INSERT INTO payment (date, amount, method, psp_payment_id, psp_type, payer_email, status) VALUES
                                                                                              ('2025-08-01 10:00:00', 100000, 'Carte bancaire', 'MP250804.0904.A01637', 'Vola', 'jean.dupont@example.com', 'SUCCEEDED'),
                                                                                              ('2025-08-02 14:30:00', 50000, 'Virement', 'MP205804.0908.D15807', 'Vola', 'marie.curie@example.com', 'SUCCEEDED'),
                                                                                              ('2025-08-03 09:15:00', 75000, 'Paypal', 'MP250804.0910.A02057', 'Vola', 'paul.martin@example.com', 'VERIFYING');

-- Insérer des dons en liant donor et payment
-- Remarque: adapter les IDs selon l'ordre d'insertion ou utiliser RETURNING id si possible
INSERT INTO donation (donor_id, payment_id) VALUES
                                                (1, 1),
                                                (2, 2),
                                                (3, 3);

-- Insérer des paiements pour aides
INSERT INTO payment (date, amount, method, psp_payment_id, psp_type, payer_email, status) VALUES
                                                                                              ('2025-08-04 11:00:00', 40000, 'Virement', 'psp200', 'Vola', 'alice.durand@example.com', 'SUCCEEDED'),
                                                                                              ('2025-08-05 16:45:00', 30000, 'Carte bancaire', 'psp201', 'Vola', 'bob.leroy@example.com', 'SUCCEEDED');

-- Insérer les aides en liant beneficiary et payment
INSERT INTO help (beneficiary_id, payment_id, description) VALUES
                                                               (1, 4, 'Accident de voiture'),
                                                               (2, 5, 'Hospitalisation suite à une chute');
