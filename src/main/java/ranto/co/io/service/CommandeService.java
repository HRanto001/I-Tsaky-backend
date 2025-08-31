package ranto.co.io.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ranto.co.io.model.Commande;
import ranto.co.io.model.CommandeDetail;
import ranto.co.io.model.Produit;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.ProduitRepository;
import ranto.co.io.repository.UtilisateurRepository;

@Service
public class CommandeService {

  private final CommandeRepository commandeRepository;
  private final ProduitRepository produitRepository;
  private final UtilisateurRepository utilisateurRepository;

  public CommandeService(
      CommandeRepository commandeRepository,
      ProduitRepository produitRepository,
      UtilisateurRepository utilisateurRepository) {
    this.commandeRepository = commandeRepository;
    this.produitRepository = produitRepository;
    this.utilisateurRepository = utilisateurRepository;
  }

  public List<Commande> findAll() {
    return commandeRepository.findAll();
  }

  public Optional<Commande> findById(Long id) {
    return commandeRepository.findById(id);
  }

  public Commande save(Commande commande) {
    // 🔑 Récupération de l'utilisateur connecté (via Spring Security)
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Utilisateur currentUser = null;

    if (auth != null && auth.isAuthenticated()) {
      String username = auth.getName(); // supposons que le username est unique
      currentUser =
          utilisateurRepository
              .findByEmail(username)
              .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + username));
    }

    if (commande.getId() != null) {
      Commande existing =
          commandeRepository
              .findById(commande.getId())
              .orElseThrow(() -> new RuntimeException("Commande introuvable"));

      commande.setCreatedBy(existing.getCreatedBy());

      if (currentUser != null) {
        commande.setUpdatedBy(currentUser);
      }
    } else {
      // Création
      commande.setDateCommande(LocalDateTime.now());
      if (currentUser != null) {
        commande.setCreatedBy(currentUser);
      }
    }

    // Vérification et calcul des détails
    if (commande.getDetails() != null) {
      for (CommandeDetail detail : commande.getDetails()) {
        if (detail.getProduit() == null || detail.getProduit().getId() == null) {
          throw new RuntimeException("Produit manquant dans le détail de commande");
        }

        Produit produit =
            produitRepository
                .findById(detail.getProduit().getId())
                .orElseThrow(
                    () ->
                        new RuntimeException(
                            "Produit introuvable : " + detail.getProduit().getId()));

        if (produit.getPrixUnitaire() == null) {
          throw new RuntimeException(
              "Le produit " + produit.getNom() + " n'a pas de prix unitaire défini");
        }

        detail.setPrixTotal(detail.getQuantite() * produit.getPrixUnitaire());
        detail.setCommande(commande);
      }
    }

    return commandeRepository.save(commande);
  }

  public void delete(Long id) {
    commandeRepository.deleteById(id);
  }
}
