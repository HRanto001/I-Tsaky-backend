package ranto.co.io.endpoint.controller.dto;

import lombok.Data;
import ranto.co.io.model.Client;

@Data
public class ClientDTO {
  private Long id;
  private String nom;
  private String typeClient;
  private String telephone;
  private String email;
  private String adresse;

  public ClientDTO(Client client) {
    this.id = client.getId();
    this.nom = client.getNom();
    this.typeClient = String.valueOf(client.getTypeClient());
    this.telephone = client.getTelephone();
    this.email = client.getEmail();
    this.adresse = client.getAdresse();
  }

  // Getters et Setters
}
