package ranto.co.io.endpoint.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import ranto.co.io.model.Commande;
import ranto.co.io.model.enums.StatutCommande;

@Data
public class CommandeDTO {
  private Long id;
  private ClientDTO client;
  private LocalDateTime dateCommande;
  private StatutCommande statut;
  private List<CommandeDetailDTO> details;
  private UtilisateurDTO createdBy;
  private UtilisateurDTO updatedBy;

  // Constructeurs
  public CommandeDTO() {}

  public CommandeDTO(Commande commande) {
    this.id = commande.getId();
    this.client = new ClientDTO(commande.getClient());
    this.dateCommande = commande.getDateCommande();
    this.statut = commande.getStatut();
    this.details =
        commande.getDetails().stream().map(CommandeDetailDTO::new).collect(Collectors.toList());
    this.createdBy = new UtilisateurDTO(commande.getCreatedBy());
    if (commande.getUpdatedBy() != null) {
      this.updatedBy = new UtilisateurDTO(commande.getUpdatedBy());
    }
  }

  // Getters et Setters
  // ... (omis pour brièveté)
}
