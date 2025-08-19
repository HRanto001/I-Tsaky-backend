package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Stock;
import ranto.co.io.repository.StockRepository;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

  private final StockRepository repository;

  public StockController(StockRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<Stock> getAll() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Stock> getById(@PathVariable Long id) {
    return repository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Stock create(@RequestBody Stock stock) {
    return repository.save(stock);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Stock> update(@PathVariable Long id, @RequestBody Stock stock) {
    return repository
        .findById(id)
        .map(
            existing -> {
              stock.setId(existing.getId());
              return ResponseEntity.ok(repository.save(stock));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    repository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
