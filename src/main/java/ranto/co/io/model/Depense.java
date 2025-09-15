package ranto.co.io.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import ranto.co.io.model.enums.TypeDepense;

@Entity
@Table(name = "depenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Depense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private TypeDepense typeDepense;

  private Double montant;
  private final LocalDate dateDepense = LocalDate.now();
  private String description;

  @ManyToOne
  @JoinColumn(name = "created_by")
  private Utilisateur createdBy;

  @ManyToOne
  @JoinColumn(name = "updated_by")
  private Utilisateur updatedBy;

  private LocalDateTime updatedAt;
}
