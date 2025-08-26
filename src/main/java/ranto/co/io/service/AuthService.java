package ranto.co.io.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ranto.co.io.model.ActivationKey;
import ranto.co.io.model.LoginRequest;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.repository.ActivationKeyRepository;
import ranto.co.io.repository.UtilisateurRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UtilisateurRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final ActivationKeyRepository keyRepository;

  public Utilisateur register(Utilisateur user, String registrationKey) {
    // Vérifier si la clé existe et n'est pas utilisée
    ActivationKey key =
        keyRepository
            .findByKeyValue(registrationKey)
            .orElseThrow(() -> new IllegalArgumentException("Clé invalide"));

    if (key.isUsed()) {
      throw new IllegalArgumentException("Cette clé a déjà été utilisée");
    }

    // Vérifier l'email
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("Email déjà utilisé");
    }

    // Vérifier mot de passe
    if (user.getMotDePasse() == null || user.getMotDePasse().isEmpty()) {
      throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
    }

    // Encoder mot de passe
    user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
    Utilisateur savedUser = userRepository.save(user);

    // Marquer la clé comme utilisée
    key.setUsed(true);
    keyRepository.save(key);

    return savedUser;
  }

  public Optional<Utilisateur> login(LoginRequest request) {
    return userRepository
        .findByEmail(request.getEmail())
        .filter(u -> passwordEncoder.matches(request.getMotDePasse(), u.getMotDePasse()));
  }
}
