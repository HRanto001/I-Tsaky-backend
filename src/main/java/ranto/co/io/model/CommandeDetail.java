package ranto.co.io.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "commande_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommandeDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JsonBackReference
  private Commande commande;

  @ManyToOne(optional = false)
  private Produit produit;

  private Integer quantite;
  private Double prixTotal;

  public CommandeDetail(long l, Commande s) {
    this.id = l;
    this.commande = s;
  }
}
