package ranto.co.io.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Historique")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Historique {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String methode; // POST, PUT, DELETE
  private String endpoint; // /api/xxx
  private String utilisateur; // email/username (si authentification)

  @Column(columnDefinition = "TEXT")
  private String payload; // corps de la requête (JSON)

  private LocalDateTime dateAction;
}
