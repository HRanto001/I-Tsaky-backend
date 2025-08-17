package ranto.co.io.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import ranto.co.io.model.enums.StatutCommande;

@Entity
@Table(name = "commandes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JsonIgnoreProperties("commandes") // évite boucle si Client contient commandes
  private Client client;

  private LocalDateTime dateCommande = LocalDateTime.now();

  @Enumerated(EnumType.STRING)
  private StatutCommande statut;

  @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<CommandeDetail> details;
}
