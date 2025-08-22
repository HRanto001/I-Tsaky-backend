package ranto.co.io.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ranto.co.io.model.LoginRequest;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.repository.UtilisateurRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UtilisateurRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public Utilisateur register(Utilisateur user) {
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new IllegalArgumentException("Email déjà utilisé");
    }
    if (user.getMotDePasse() == null || user.getMotDePasse().isEmpty()) {
      throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
    }
    user.setMotDePasse(passwordEncoder.encode(user.getMotDePasse()));
    return userRepository.save(user);
  }

  public Optional<Utilisateur> login(LoginRequest request) {
    return userRepository
        .findByEmail(request.getEmail())
        .filter(u -> passwordEncoder.matches(request.getMotDePasse(), u.getMotDePasse()));
  }
}
