package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.Role;
import ranto.co.io.repository.UtilisateurRepository;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

  private final UtilisateurRepository repository;

  public UtilisateurController(UtilisateurRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Utilisateur> getAll() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Utilisateur> getById(@PathVariable Long id) {
    return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/role/{role}")
  public List<Utilisateur> getByRole(@PathVariable Role role) {
    return repository.findByRole(role);
  }

  @PostMapping
  public Utilisateur create(@RequestBody Utilisateur utilisateur) {
    return repository.save(utilisateur);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Utilisateur> update(
      @PathVariable Long id, @RequestBody Utilisateur utilisateur) {
    return repository
        .findById(id)
        .map(
            existing -> {
              utilisateur.setId(existing.getId());
              return ResponseEntity.ok(repository.save(utilisateur));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
