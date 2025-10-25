package ranto.co.io.endpoint.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.endpoint.controller.dto.CommandeDTO;
import ranto.co.io.endpoint.controller.dto.DashboardStatsDTO;
import ranto.co.io.model.Commande;
import ranto.co.io.model.CommandeDetail;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.Role;
import ranto.co.io.model.enums.StatutCommande;
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
            StockRepository stockRepository,
            UtilisateurRepository utilisateurRepository) {
        this.commandeService = commandeService;
        this.commandeRepository = commandeRepository;
        this.stockRepository = stockRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CommandeDTO> getAllCommandes() {
        return commandeService.findAll().stream()
                .map(CommandeDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN','VENTE','PRODUCTION','MARKETING')")
    public ResponseEntity<Page<CommandeDTO>> getPagedCommandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        String username = authentication.getName();
        Utilisateur user = utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        PageRequest pageable = PageRequest.of(page, size, Sort.by("dateCommande").descending());

        Page<Commande> commandes;

        if (user.getRole() == Role.ADMIN) {
            commandes = commandeService.findAllPaged(pageable);
        } else {
            commandes = commandeService.findByUserPaged(user, pageable);
        }

        // Convertir en DTO
        Page<CommandeDTO> commandeDTOs = commandes.map(CommandeDTO::new);
        return ResponseEntity.ok(commandeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id) {
        return commandeService
                .findById(id)
                .map(CommandeDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CommandeDTO createCommande(@RequestBody Commande commande) {
        Commande saved = commandeService.save(commande);
        return new CommandeDTO(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommandeDTO> updateCommande(
            @PathVariable Long id, @RequestBody Commande commande) {
        return commandeService
                .findById(id)
                .map(existing -> {
                    commande.setId(existing.getId());
                    Commande saved = commandeService.save(commande);
                    return ResponseEntity.ok(new CommandeDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN','VENTE','PRODUCTION','MARKETING')")
    public Collection<Map<String, ? extends Serializable>> getRecentOrders(
            Authentication authentication) {
        String username = authentication.getName();
        Utilisateur user = utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Commande> commandes;

        if (user.getRole() == Role.ADMIN) {
            commandes = commandeRepository.findTop10ByOrderByDateCommandeDesc();
        } else {
            commandes = commandeRepository.findTop10ByCreatedByOrderByDateCommandeDesc(user);
        }

        return commandes.stream()
                .map(c -> Map.of(
                        "id", c.getId(),
                        "client", c.getClient().getNom(),
                        "total", c.getDetails().stream().mapToDouble(CommandeDetail::getPrixTotal).sum(),
                        "status", c.getStatut().name(),
                        "date", c.getDateCommande().toLocalDate().toString()))
                .collect(Collectors.toList());
    }

    @GetMapping("/mes-commandes")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENTE', 'PRODUCTION', 'MARKETING')")
    public ResponseEntity<List<CommandeDTO>> getMesCommandes(Authentication authentication) {
        String username = authentication.getName();
        Utilisateur user = utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Commande> commandes = commandeRepository.findByCreatedBy(user);
        List<CommandeDTO> commandeDTOs = commandes.stream()
                .map(CommandeDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commandeDTOs);
    }

    @GetMapping("/mes-commandes2")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENTE', 'PRODUCTION', 'MARKETING')")
    public ResponseEntity<Page<CommandeDTO>> getMesCommandes(
            Authentication authentication, Pageable pageable) {

        String username = authentication.getName();
        Utilisateur user = utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Page<Commande> commandes = commandeRepository.findByCreatedBy(user, pageable);
        Page<CommandeDTO> commandeDTOs = commandes.map(CommandeDTO::new);
        return ResponseEntity.ok(commandeDTOs);
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN','VENTE')")
    public ResponseEntity<CommandeDTO> changerStatut(
            @PathVariable Long id, @RequestParam StatutCommande statut) {

        Commande updated = commandeService.changerStatut(id, statut);
        return ResponseEntity.ok(new CommandeDTO(updated));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<CommandeDTO> updateStatut(
            @PathVariable Long id, @RequestParam StatutCommande nouveauStatut) {
        Commande commande = commandeService.updateStatut(id, nouveauStatut);
        return ResponseEntity.ok(new CommandeDTO(commande));
    }

    @GetMapping("/stats")
    public DashboardStatsDTO getStats(Authentication authentication) {
        String username = authentication.getName();
        Utilisateur user = utilisateurRepository
                .findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        List<Commande> commandes;

        if (user.getRole() == Role.ADMIN) {
            commandes = commandeRepository.findAll();
        } else {
            commandes = commandeRepository.findByCreatedBy(user);
        }

        long clientsActifs = commandes.stream().map(c -> c.getClient().getId()).distinct().count();
        long produitsEnStock = stockRepository.count();
        double chiffreAffaires = commandes.stream()
                .mapToDouble(c -> c.getDetails().stream().mapToDouble(CommandeDetail::getPrixTotal).sum())
                .sum();
        long totalCommandes = commandes.size();

        return new DashboardStatsDTO(clientsActifs, produitsEnStock, chiffreAffaires, totalCommandes);
    }
}