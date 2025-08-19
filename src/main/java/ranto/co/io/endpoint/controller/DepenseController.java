package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Depense;
import ranto.co.io.model.enums.TypeDepense;
import ranto.co.io.repository.DepenseRepository;

@RestController
@RequestMapping("/api/depenses")
public class DepenseController {

  private final DepenseRepository repository;

  public DepenseController(DepenseRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Depense> getAll() {
    return repository.findAll();
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
    return repository.save(depense);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Depense> update(@PathVariable Long id, @RequestBody Depense depense) {
    return repository
        .findById(id)
        .map(
            existing -> {
              depense.setId(existing.getId());
              return ResponseEntity.ok(repository.save(depense));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
