package ranto.co.io.endpoint.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.endpoint.controller.dto.ResetPasswordRequest;
import ranto.co.io.model.LoginRequest;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.repository.UtilisateurRepository;
import ranto.co.io.security.JwtUtil;
import ranto.co.io.service.ActivationKeyService;
import ranto.co.io.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtUtil jwtUtil;
  private final UtilisateurRepository utilisateurRepository;
  private final ActivationKeyService activationKeyService;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestParam("activationKey") String activationKey, @RequestBody Utilisateur utilisateur) {
    try {
      // vérification de la clé
      Utilisateur savedUser = authService.register(utilisateur, activationKey);

      // Récupérer le rôle sous forme de String
      String userRole = savedUser.getRole().name();

      // Générer le token avec email (subject) et rôle (claim)
      String token = jwtUtil.generateToken(savedUser.getEmail(), userRole);

      // réponse JSON
      return ResponseEntity.ok(
          Map.of(
              "id", savedUser.getId(),
              "nom", savedUser.getNom(),
              "email", savedUser.getEmail(),
              "role", savedUser.getRole(),
              "token", token,
              "message", "Utilisateur créé et connecté avec succès"));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of("error", "Erreur interne"));
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<Utilisateur> user = authService.login(request);
    if (user.isPresent()) {
      Utilisateur u = user.get();
      String token = jwtUtil.generateToken(u.getEmail(), u.getRole().name());

      return ResponseEntity.ok(
          Map.of(
              "message",
              "Connexion réussie pour " + u.getNom(),
              "email",
              u.getEmail(),
              "role",
              u.getRole(),
              "token",
              token));
    } else {
      return ResponseEntity.status(401).body(Map.of("error", "Email ou mot de passe incorrect"));
    }
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(
      @RequestHeader(value = "Authorization", required = false) String authHeader) {

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return ResponseEntity.status(401).body(Map.of("error", "Token manquant ou invalide"));
    }

    String token = authHeader.substring(7);

    if (!jwtUtil.validateToken(token)) {
      return ResponseEntity.status(401).body(Map.of("error", "Token invalide ou expiré"));
    }

    String email = jwtUtil.getEmailFromToken(token);

    return utilisateurRepository
        .findByEmail(email)
        .map(
            user ->
                ResponseEntity.ok(
                    Map.of(
                        "id", user.getId(),
                        "nom", user.getNom(),
                        "prenom", user.getPrenom(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "actif", user.getActif())))
        .orElse(ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé")));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    // Vérifier si l'utilisateur existe
    return utilisateurRepository
        .findByEmail(request.getEmail())
        .map(
            user -> {
              // Chercher la clé
              return activationKeyService
                  .findByValue(request.getActivationKey())
                  .map(
                      key -> {
                        // Vérifier expiration
                        if (key.getExpiresAt() != null
                            && key.getExpiresAt().isBefore(LocalDateTime.now())) {
                          return ResponseEntity.badRequest().body(Map.of("error", "Clé expirée"));
                        }

                        // Vérifier si déjà utilisée
                        if (key.isUsed()) {
                          return ResponseEntity.badRequest()
                              .body(Map.of("error", "Clé déjà utilisée"));
                        }

                        // Tout est bon → on met à jour le mot de passe
                        user.setMotDePasse(passwordEncoder.encode(request.getNewPassword()));
                        utilisateurRepository.save(user);

                        // marquer la clé comme utilisée
                        key.setUsed(true);
                        activationKeyService.save(key);

                        return ResponseEntity.ok(
                            Map.of("message", "Mot de passe réinitialisé avec succès"));
                      })
                  .orElse(
                      ResponseEntity.badRequest()
                          .body(Map.of("error", "Clé d'activation introuvable")));
            })
        .orElse(ResponseEntity.status(404).body(Map.of("error", "Utilisateur introuvable")));
  }

  @PostMapping("/request-reset-password")
  public ResponseEntity<?> requestReset(@RequestParam String email) {

    activationKeyService.generateAndSendResetKey(email);

    return ResponseEntity.ok(Map.of("message", "Code de réinitialisation envoyé par email"));
  }

  @PostMapping("/request-activation-key")
  public ResponseEntity<?> requestActivationKey(@RequestParam String email) {

    activationKeyService.generateAndSendActivationKey(email);

    return ResponseEntity.ok(Map.of("message", "Clé d’activation envoyée par email"));
  }

  @GetMapping("/check-email")
  public ResponseEntity<?> checkEmail(@RequestParam String email) {

    if (email == null || email.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of("error", "Email requis"));
    }

    boolean exists = utilisateurRepository.existsByEmail(email);

    return ResponseEntity.ok(
        Map.of(
            "email", email,
            "exists", exists));
  }
}
