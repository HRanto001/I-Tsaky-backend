package ranto.co.io.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ranto.co.io.model.Commande;
import ranto.co.io.model.CommandeDetail;
import ranto.co.io.model.Produit;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.ProduitRepository;

@Service
public class CommandeService {

  private final CommandeRepository commandeRepository;
  private final ProduitRepository produitRepository;

  public CommandeService(
      CommandeRepository commandeRepository, ProduitRepository produitRepository) {
    this.commandeRepository = commandeRepository;
    this.produitRepository = produitRepository;
  }

  public List<Commande> findAll() {
    return commandeRepository.findAll();
  }

  public Optional<Commande> findById(Long id) {
    return commandeRepository.findById(id);
  }

  public Commande save(Commande commande) {
    if (commande.getId() == null) {
      commande.setDateCommande(LocalDateTime.now()); // seulement à la création
    }

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
