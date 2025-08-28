package ranto.co.io.endpoint.controller;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.LoginRequest;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.repository.UtilisateurRepository;
import ranto.co.io.security.JwtUtil;
import ranto.co.io.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtUtil jwtUtil;
  private final UtilisateurRepository utilisateurRepository;

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
                        "email", user.getEmail(),
                        "role", user.getRole())))
        .orElse(ResponseEntity.status(404).body(Map.of("error", "Utilisateur non trouvé")));
  }
}
