package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.CommandeDetail;
import ranto.co.io.repository.CommandeDetailRepository;

@RestController
@RequestMapping("/api/commande-details")
public class CommandeDetailController {

  private final CommandeDetailRepository repository;

  public CommandeDetailController(CommandeDetailRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<CommandeDetail> getAll() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<CommandeDetail> getById(@PathVariable Long id) {
    return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public CommandeDetail create(@RequestBody CommandeDetail detail) {
    return repository.save(detail);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CommandeDetail> update(
      @PathVariable Long id, @RequestBody CommandeDetail detail) {
    return repository
        .findById(id)
        .map(
            existing -> {
              detail.setId(existing.getId());
              return ResponseEntity.ok(repository.save(detail));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
