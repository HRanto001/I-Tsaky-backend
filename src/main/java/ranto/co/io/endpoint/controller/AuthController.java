package ranto.co.io.endpoint.controller;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.LoginRequest;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<Utilisateur> register(@RequestBody Utilisateur utilisateur) {
    Utilisateur savedUser = authService.register(utilisateur);
    return ResponseEntity.ok(savedUser);
  }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Utilisateur> user = authService.login(request);
        if (user.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "message", "Connexion réussie pour " + user.get().getNom(),
                    "email", user.get().getEmail(),
                    "role", user.get().getRole()
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Email ou mot de passe incorrect"
            ));
        }
    }

}
