package ranto.co.io.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ranto.co.io.model.Produit;
import ranto.co.io.model.enums.CategorieProduit;
import ranto.co.io.repository.ProduitRepository;

@Service
public class ProduitService {

  private final ProduitRepository produitRepository;

  public ProduitService(ProduitRepository produitRepository) {
    this.produitRepository = produitRepository;
  }

  public List<Produit> findAll() {
    return produitRepository.findAll();
  }

  public Optional<Produit> findById(Long id) {
    return produitRepository.findById(id);
  }

  public List<Produit> findByCategorie(CategorieProduit categorie) {
    return produitRepository.findByCategorie(categorie);
  }

  public Produit save(Produit produit) {
    return produitRepository.save(produit);
  }

  public void delete(Long id) {
    produitRepository.deleteById(id);
  }

  public Produit updateImage(Long id, byte[] imageData, String imageType) {
    Produit produit =
        produitRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
    produit.setImageData(imageData);
    produit.setImageType(imageType);
    return produitRepository.save(produit);
  }
}
