package ranto.co.io.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "activation_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationKey {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String keyValue;

  @Column(nullable = false)
  @Builder.Default
  private boolean used = false;

  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;
}
