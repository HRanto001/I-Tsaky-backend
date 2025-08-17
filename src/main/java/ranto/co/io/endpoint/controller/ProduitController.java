package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Produit;
import ranto.co.io.model.enums.CategorieProduit;
import ranto.co.io.service.ProduitService;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

  private final ProduitService produitService;

  public ProduitController(ProduitService produitService) {
    this.produitService = produitService;
  }

  @GetMapping
  public List<Produit> getAllProduits() {
    return produitService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
    return produitService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/categorie/{categorie}")
  public List<Produit> getByCategorie(@PathVariable CategorieProduit categorie) {
    return produitService.findByCategorie(categorie);
  }

  @PostMapping
  public Produit createProduit(@RequestBody Produit produit) {
    return produitService.save(produit);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Produit> updateProduit(
      @PathVariable Long id, @RequestBody Produit produit) {
    return produitService
        .findById(id)
        .map(
            existing -> {
              produit.setId(existing.getId());
              return ResponseEntity.ok(produitService.save(produit));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
    produitService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
