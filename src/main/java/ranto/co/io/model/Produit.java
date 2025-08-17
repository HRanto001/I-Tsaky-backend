package ranto.co.io.model;

import jakarta.persistence.*;
import lombok.*;
import ranto.co.io.model.enums.CategorieProduit;

@Entity
@Table(name = "produits")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Double prixUnitaire;

    @Enumerated(EnumType.STRING)
    private CategorieProduit categorie;

    private Integer stockDisponible;
}
