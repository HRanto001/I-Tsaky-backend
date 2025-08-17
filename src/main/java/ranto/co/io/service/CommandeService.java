package ranto.co.io.service;

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
    // Calcul automatique du prix total
    for (CommandeDetail detail : commande.getDetails()) {
      Produit produit =
          produitRepository
              .findById(detail.getProduit().getId())
              .orElseThrow(
                  () ->
                      new RuntimeException("Produit introuvable : " + detail.getProduit().getId()));
      detail.setPrixTotal(detail.getQuantite() * produit.getPrixUnitaire());
      detail.setCommande(commande);
    }
    return commandeRepository.save(commande);
  }

  public void delete(Long id) {
    commandeRepository.deleteById(id);
  }
}
