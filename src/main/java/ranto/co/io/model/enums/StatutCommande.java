package ranto.co.io.model.enums;

public enum StatutCommande {
  EN_ATTENTE,
  PAYEE,
  LIVREE,
  ANNULEE;

  // Vérifie si la transition est autorisée
  public boolean peutChangerVers(StatutCommande nouveauStatut) {
    switch (this) {
      case EN_ATTENTE:
        return nouveauStatut == PAYEE || nouveauStatut == ANNULEE;
      case PAYEE:
        return nouveauStatut == LIVREE; // pas d'annulation après paiement
      case LIVREE:
        return false; // une commande livrée est définitive
      case ANNULEE:
        return false; // une commande annulée reste annulée
      default:
        return false;
    }
  }
}
