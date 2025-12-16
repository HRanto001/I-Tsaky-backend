package ranto.co.io.endpoint.controller.dto;

import lombok.Data;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.Role;

@Data
public class UtilisateurDTO {
  private Long id;
  private String nom;
  private String prenom;
  private Role role;
  private String email;

  public UtilisateurDTO(Utilisateur utilisateur) {
    this.id = utilisateur.getId();
    this.nom = utilisateur.getNom();
    this.prenom = utilisateur.getPrenom();
    this.role = utilisateur.getRole();
    this.email = utilisateur.getEmail();
  }
}
