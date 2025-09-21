ALTER TABLE commandes
ADD CONSTRAINT commandes_statut_check
CHECK (statut IN ('EN_ATTENTE', 'ACCEPTE', 'ANNULEE', 'PAYEE', 'LIVREE'));
