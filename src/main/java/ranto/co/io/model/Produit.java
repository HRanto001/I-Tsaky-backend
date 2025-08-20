package ranto.co.io.model;

import jakarta.persistence.*;
import lombok.*;
import ranto.co.io.model.enums.CategorieProduit;

@Entity
@Table(name = "produits")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Produit {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  @Column(name = "prix_unitaire")
  private Double prixUnitaire;

  @Enumerated(EnumType.STRING)
  private CategorieProduit categorie;

  @Column(name = "stock_disponible")
  private Integer stockDisponible;
}