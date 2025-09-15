package ranto.co.io.endpoint.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Depense;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.TypeDepense;
import ranto.co.io.repository.DepenseRepository;
import ranto.co.io.repository.UtilisateurRepository;
import ranto.co.io.service.DepenseService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/depenses")
public class DepenseController {

  private final DepenseRepository repository;
  private final UtilisateurRepository utilisateurRepository;
  private final DepenseService depenseService;

  @GetMapping
  public List<Depense> getAll() {
    return repository.findAll();
  }

  @GetMapping("/paged")
  public Page<Depense> getAllPaged(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return repository.findAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Depense> getById(@PathVariable Long id) {
    return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/type/{type}")
  public List<Depense> getByType(@PathVariable TypeDepense type) {
    return repository.findByTypeDepense(type);
  }

  @PostMapping
  public Depense create(@RequestBody Depense depense) {
    return depenseService.save(depense);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Depense> update(@PathVariable Long id, @RequestBody Depense depense) {
    try {
      Depense updated = depenseService.update(id, depense);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/mes-depenses")
  @PreAuthorize("hasAnyRole('ADMIN', 'VENTE', 'PRODUCTION', 'MARKETING')")
  public ResponseEntity<Page<Depense>> getMesDepenses(
      Authentication authentication, Pageable pageable) {
    // Récupérer l'utilisateur connecté
    String username = authentication.getName();
    Utilisateur user =
        utilisateurRepository
            .findByEmail(username)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    // Retourner uniquement les dépenses de cet utilisateur
    Page<Depense> depenses = repository.findByCreatedBy(user, pageable);

    return ResponseEntity.ok(depenses);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
