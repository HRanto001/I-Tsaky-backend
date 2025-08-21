package ranto.co.io.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ranto.co.io.model.LoginRequest;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.repository.UtilisateurRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordEncoder passwordEncoder;

  public Utilisateur register(Utilisateur utilisateur) {
    // Encoder le mot de passe
    utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
    return utilisateurRepository.save(utilisateur);
  }

  public Optional<Utilisateur> login(LoginRequest request) {
    return utilisateurRepository
        .findByEmail(request.getEmail())
        .filter(user -> passwordEncoder.matches(request.getMotDePasse(), user.getMotDePasse()));
  }
}
