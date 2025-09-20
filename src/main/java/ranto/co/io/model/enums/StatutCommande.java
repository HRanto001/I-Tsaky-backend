package ranto.co.io.model.enums;

public enum StatutCommande {
    EN_ATTENTE,
    ACCEPTE,
    PAYEE,
    LIVREE,
    ANNULEE;

    public boolean peutChangerVers(StatutCommande nouveauStatut) {
        switch (this) {
            case EN_ATTENTE:
                // Depuis EN_ATTENTE → on peut encore modifier les détails
                return nouveauStatut == ACCEPTE || nouveauStatut == ANNULEE;
            case ACCEPTE:
                // Une commande acceptée ne peut plus être modifiée,
                // mais on peut la payer ou l’annuler
                return nouveauStatut == PAYEE || nouveauStatut == ANNULEE;
            case PAYEE:
                return nouveauStatut == LIVREE;
            case LIVREE:
            case ANNULEE:
                return false;
            default:
                return false;
        }
    }
}
