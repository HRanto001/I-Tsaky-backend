package ranto.co.io.model;

import jakarta.persistence.*;
import java.time.LocalDate;
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
  private LocalDate dateDepense = LocalDate.now();
  private String description;
}
