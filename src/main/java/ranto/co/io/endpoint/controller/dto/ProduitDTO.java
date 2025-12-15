package ranto.co.io.endpoint.controller.dto;

import lombok.Data;
import ranto.co.io.model.Produit;
import ranto.co.io.model.enums.CategorieProduit;

@Data
public class ProduitDTO {
  private Long id;
  private String nom;
  private CategorieProduit categorie;
  private Double prixUnitaire;
  private Integer stockDisponible;
  private Boolean hasImage;

  public ProduitDTO(Produit produit) {
    this.id = produit.getId();
    this.nom = produit.getNom();
    this.categorie = produit.getCategorie();
    this.prixUnitaire = produit.getPrixUnitaire();
    this.stockDisponible = produit.getStockDisponible();
    this.hasImage = produit.getImageData() != null && produit.getImageType() != null;
  }
}
