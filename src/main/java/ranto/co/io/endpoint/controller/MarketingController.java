package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Marketing;
import ranto.co.io.model.enums.CanalMarketing;
import ranto.co.io.repository.MarketingRepository;

@RestController
@RequestMapping("/api/marketing")
public class MarketingController {

  private final MarketingRepository repository;

  public MarketingController(MarketingRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Marketing> getAll() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Marketing> getById(@PathVariable Long id) {
    return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/canal/{canal}")
  public List<Marketing> getByCanal(@PathVariable CanalMarketing canal) {
    return repository.findByCanal(canal);
  }

  @PostMapping
  public Marketing create(@RequestBody Marketing marketing) {
    return repository.save(marketing);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Marketing> update(@PathVariable Long id, @RequestBody Marketing marketing) {
    return repository
        .findById(id)
        .map(
            existing -> {
              marketing.setId(existing.getId());
              return ResponseEntity.ok(repository.save(marketing));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
