package ranto.co.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Produit;
import ranto.co.io.model.enums.CategorieProduit;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCategorie(CategorieProduit categorie);
    List<Produit> findByNomContainingIgnoreCase(String nom);
}
