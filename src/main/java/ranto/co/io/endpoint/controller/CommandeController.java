package ranto.co.io.endpoint.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.endpoint.controller.dto.DashboardStatsDTO;
import ranto.co.io.model.Commande;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.StockRepository;
import ranto.co.io.service.CommandeService;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

  private final CommandeService commandeService;
  private final CommandeRepository commandeRepository;
  private final StockRepository stockRepository;

  public CommandeController(
      CommandeService commandeService,
      CommandeRepository commandeRepository,
      StockRepository stockRepository) {
    this.commandeService = commandeService;
    this.commandeRepository = commandeRepository;
    this.stockRepository = stockRepository;
  }

  @GetMapping
  public List<Commande> getAllCommandes() {
    return commandeService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
    return commandeService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Commande createCommande(@RequestBody Commande commande) {
    return commandeService.save(commande);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Commande> updateCommande(
      @PathVariable Long id, @RequestBody Commande commande) {
    return commandeService
        .findById(id)
        .map(
            existing -> {
              commande.setId(existing.getId());
              return ResponseEntity.ok(commandeService.save(commande));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
    commandeService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/recent")
  public Collection<Map<String, ? extends Serializable>> getRecentOrders() {
    return commandeRepository.findTop10ByOrderByDateCommandeDesc().stream()
        .map(
            c ->
                Map.of(
                    "id", c.getId(),
                    "client", c.getClient().getNom(),
                    "total",
                        c.getDetails().stream()
                            .mapToDouble(d -> d.getPrixTotal() * d.getQuantite())
                            .sum(),
                    "status", c.getStatut().name(),
                    "date", c.getDateCommande().toLocalDate().toString()))
        .collect(Collectors.toList());
  }

  @GetMapping("/stats")
  public DashboardStatsDTO getStats() {

    long clientsActifs =
        commandeRepository.findAll().stream().map(c -> c.getClient().getId()).distinct().count();

    long produitsEnStock = stockRepository.count();

    double chiffreAffaires =
        commandeRepository.findAll().stream()
            .mapToDouble(
                c ->
                    c.getDetails().stream()
                        .mapToDouble(d -> d.getPrixTotal() * d.getQuantite())
                        .sum())
            .sum();

    long totalCommandes = commandeRepository.count();

    return new DashboardStatsDTO(clientsActifs, produitsEnStock, chiffreAffaires, totalCommandes);
  }
}
