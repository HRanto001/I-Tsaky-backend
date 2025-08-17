package ranto.co.io.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Produit;
import ranto.co.io.model.enums.CategorieProduit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
  List<Produit> findByCategorie(CategorieProduit categorie);

  List<Produit> findByNomContainingIgnoreCase(String nom);
}
