package ranto.co.io.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import ranto.co.io.model.enums.CanalMarketing;

@Entity
@Table(name = "marketing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marketing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private CanalMarketing canal;

  private Double cout;
  private LocalDate dateAction = LocalDate.now();
  private String description;
}
