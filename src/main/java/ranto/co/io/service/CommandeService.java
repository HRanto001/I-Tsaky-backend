package ranto.co.io.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ranto.co.io.model.Commande;
import ranto.co.io.model.CommandeDetail;
import ranto.co.io.model.Produit;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.StatutCommande;
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

  public Page<Commande> findAllPaged(Pageable pageable) {
    return commandeRepository.findAllByOrderByDateCommandeDesc(pageable);
  }

  public Page<Commande> findByUserPaged(Utilisateur user, Pageable pageable) {
    return commandeRepository.findByCreatedByOrderByDateCommandeDesc(user, pageable);
  }

    public Commande save(Commande commande) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur currentUser = null;

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            currentUser = utilisateurRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + username));
        }

        if (commande.getId() != null) {
            // Mise à jour
            Commande existing = commandeRepository.findById(commande.getId())
                    .orElseThrow(() -> new RuntimeException("Commande introuvable"));

            commande.setCreatedBy(existing.getCreatedBy());

            if (existing.getStatut() != StatutCommande.EN_ATTENTE) {
                commande.setDetails(existing.getDetails()); // garder les anciens détails
            }

            if (currentUser != null) {
                commande.setUpdatedBy(currentUser);
            }
        } else {
            // Création
            commande.setDateCommande(LocalDateTime.now());
            if (currentUser != null) {
                commande.setCreatedBy(currentUser);

                // 🔥 Activer l'utilisateur une seule fois ici
                if (Boolean.FALSE.equals(currentUser.getActif())) {
                    currentUser.setActif(true);
                    utilisateurRepository.save(currentUser);
                }
            }
        }

        // Vérification et calcul des détails
        if (commande.getDetails() != null) {
            for (CommandeDetail detail : commande.getDetails()) {
                Produit produit = produitRepository.findById(detail.getProduit().getId())
                        .orElseThrow(() -> new RuntimeException("Produit introuvable : " + detail.getProduit().getId()));

                int ancienneQuantite = 0;
                if (detail.getId() != null) {
                    CommandeDetail oldDetail = commandeRepository.findById(commande.getId())
                            .flatMap(c -> c.getDetails().stream()
                                    .filter(d -> d.getId().equals(detail.getId()))
                                    .findFirst())
                            .orElse(null);
                    if (oldDetail != null) {
                        ancienneQuantite = oldDetail.getQuantite();
                    }
                }

                int difference = detail.getQuantite() - ancienneQuantite;

                // Vérifier le stock uniquement si on augmente la quantité
                if (difference > 0 && produit.getStockDisponible() < difference) {
                    throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNom());
                }

                // Mise à jour du stock
                produit.setStockDisponible(produit.getStockDisponible() - difference);
                produitRepository.save(produit);

                // Prix total
                detail.setPrixTotal(detail.getQuantite() * produit.getPrixUnitaire());
                detail.setCommande(commande);
            }
        }

        return commandeRepository.save(commande);
    }

    public Commande changerStatut(Long commandeId, StatutCommande nouveauStatut) {
    Commande commande =
        commandeRepository
            .findById(commandeId)
            .orElseThrow(() -> new RuntimeException("Commande introuvable"));

    if (!commande.getStatut().peutChangerVers(nouveauStatut)) {
      throw new RuntimeException(
          "Transition de statut non autorisée : " + commande.getStatut() + " -> " + nouveauStatut);
    }

    commande.setStatut(nouveauStatut);
    return commandeRepository.save(commande);
  }

  @Transactional
  public Commande updateStatut(Long id, StatutCommande nouveauStatut) {
    Commande commande =
        commandeRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

    switch (commande.getStatut()) {
      case EN_ATTENTE:
        if (nouveauStatut != StatutCommande.ACCEPTE
            && nouveauStatut != StatutCommande.ANNULEE
            && nouveauStatut != StatutCommande.PAYEE) {

          throw new RuntimeException("Transition non autorisée depuis EN_ATTENTE");
        }
        break;
      case ACCEPTE:
        if (nouveauStatut != StatutCommande.PAYEE && nouveauStatut != StatutCommande.ANNULEE) {
          throw new RuntimeException("Transition non autorisée depuis ACCEPTEE");
        }
        break;
      case PAYEE:
        if (nouveauStatut != StatutCommande.LIVREE) {
          throw new RuntimeException("Transition non autorisée depuis PAYEE");
        }
        break;
      case LIVREE:
      case ANNULEE:
        throw new RuntimeException("Impossible de modifier une commande livrée ou annulée");
    }

    // Si la commande est annulée → restituer le stock
    if (nouveauStatut == StatutCommande.ANNULEE && commande.getDetails() != null) {
      for (CommandeDetail detail : commande.getDetails()) {
        detail.setPrixTotal(0.0);
        Produit produit = detail.getProduit();
        if (produit != null) {
          produit.setStockDisponible(produit.getStockDisponible() + detail.getQuantite());
          produitRepository.save(produit);
        }
      }
    }

    commande.setStatut(nouveauStatut);
    return commandeRepository.save(commande);
  }

  public void delete(Long id) {
    Commande commande =
        commandeRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Commande introuvable"));

    Utilisateur user = commande.getCreatedBy();

    commandeRepository.deleteById(id);

    // Vérifie si l'utilisateur n'a plus de commande
    long count = commandeRepository.countByCreatedBy(user);
    if (count == 0 && Boolean.TRUE.equals(user.getActif())) {
      user.setActif(false);
      utilisateurRepository.save(user);
    }
  }
}
