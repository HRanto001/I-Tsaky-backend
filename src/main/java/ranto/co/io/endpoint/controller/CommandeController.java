package ranto.co.io.endpoint.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.endpoint.controller.dto.DashboardStatsDTO;
import ranto.co.io.model.Commande;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.Role;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.StockRepository;
import ranto.co.io.repository.UtilisateurRepository;
import ranto.co.io.service.CommandeService;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

  private final CommandeService commandeService;
  private final CommandeRepository commandeRepository;
  private final StockRepository stockRepository;
  private final UtilisateurRepository utilisateurRepository;

  public CommandeController(
          CommandeService commandeService,
          CommandeRepository commandeRepository,
          StockRepository stockRepository, UtilisateurRepository utilisateurRepository) {
    this.commandeService = commandeService;
    this.commandeRepository = commandeRepository;
    this.stockRepository = stockRepository;
      this.utilisateurRepository = utilisateurRepository;
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('ADMIN','VENTE','PRODUCTION','MARKETING')")
    public Collection<Map<String, ? extends Serializable>> getRecentOrders(Authentication authentication) {
        String username = authentication.getName();
        Utilisateur user = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Commande> commandes;

        if(user.getRole() == Role.ADMIN) {
            // admin voit toutes les commandes
            commandes = commandeRepository.findTop10ByOrderByDateCommandeDesc();
        } else {
            // les autres voient seulement leurs commandes
            commandes = commandeRepository.findTop10ByCreatedByOrderByDateCommandeDesc(user);
        }

        return commandes.stream()
                .map(c -> Map.of(
                        "id", c.getId(),
                        "client", c.getClient().getNom(),
                        "total", c.getDetails().stream().mapToDouble(d -> d.getPrixTotal() * d.getQuantite()).sum(),
                        "status", c.getStatut().name(),
                        "date", c.getDateCommande().toLocalDate().toString()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/mes-commandes")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENTE', 'PRODUCTION', 'MARKETING')")
    public ResponseEntity<List<Commande>> getMesCommandes(Authentication authentication) {
        String username = authentication.getName();
        Utilisateur user = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Commande> commandes = commandeRepository.findByCreatedBy(user);
        return ResponseEntity.ok(commandes);
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
