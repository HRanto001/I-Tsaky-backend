package ranto.co.io.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
  private String nom;
  private String prenom;
  private String email;
  private String motDePasse;
}
