package ranto.co.io.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.Role;
import ranto.co.io.repository.ActivationKeyRepository;
import ranto.co.io.repository.UtilisateurRepository;
import ranto.co.io.service.ActivationKeyService;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

  private final ActivationKeyService activationKeyService;
  private final ActivationKeyRepository activationKeyRepository;
  private final UtilisateurRepository utilisateurRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public void run(String... args) {
    createDefaultActivationKeyIfNotExists();
    createDefaultAdminIfNotExists();
  }

  /** Crée la clé d'activation par défaut si elle n'existe pas encore */
  private void createDefaultActivationKeyIfNotExists() {
    String defaultKey = "RAN123TO";

    boolean exists = activationKeyRepository.existsByKeyValue(defaultKey);

    if (!exists) {
      activationKeyService.createKey(defaultKey, 1);
      System.out.println("Clé d’activation par défaut créée : " + defaultKey);
    } else {
      System.out.println("Clé d’activation déjà existante : " + defaultKey);
    }
  }

  /** Crée un utilisateur admin par défaut s’il n’existe pas encore */
  private void createDefaultAdminIfNotExists() {
    String defaultEmail = "hei.ranto.2@gmail.com";

    boolean exists = utilisateurRepository.existsByEmail(defaultEmail);

    if (!exists) {
      Utilisateur admin =
          Utilisateur.builder()
              .nom("Admin")
              .prenom("Etsako")
              .email(defaultEmail)
              .motDePasse(passwordEncoder.encode("admin123"))
              .role(Role.ADMIN)
              .actif(true)
              .build();

      utilisateurRepository.save(admin);
      System.out.println("Utilisateur admin créé avec succès !");
    } else {
      System.out.println("Utilisateur admin déjà existant, aucune création nécessaire.");
    }
  }
}
