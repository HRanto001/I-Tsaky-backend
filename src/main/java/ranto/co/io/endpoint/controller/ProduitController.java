package ranto.co.io.endpoint.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ranto.co.io.model.Produit;
import ranto.co.io.model.enums.CategorieProduit;
import ranto.co.io.service.ProduitService;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "*") // Ajout pour gérer les requêtes cross-origin
public class ProduitController {

  private final ProduitService produitService;

  public ProduitController(ProduitService produitService) {
    this.produitService = produitService;
  }

  @GetMapping
  public ResponseEntity<List<Produit>> getAllProduits() {
    try {
      List<Produit> produits = produitService.findAll();
      return ResponseEntity.ok(produits);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
    try {
      return produitService
          .findById(id)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/categorie/{categorie}")
  public ResponseEntity<List<Produit>> getByCategorie(@PathVariable CategorieProduit categorie) {
    try {
      List<Produit> produits = produitService.findByCategorie(categorie);
      return ResponseEntity.ok(produits);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createProduit(
      @RequestParam("nom") String nom,
      @RequestParam("categorie") CategorieProduit categorie,
      @RequestParam("prixUnitaire") Double prixUnitaire,
      @RequestParam("stockDisponible") Integer stockDisponible,
      @RequestParam(value = "image", required = false) MultipartFile image) {

    try {
      // Validation des paramètres obligatoires
      if (nom == null || nom.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Le nom est obligatoire");
      }
      if (prixUnitaire == null || prixUnitaire <= 0) {
        return ResponseEntity.badRequest().body("Le prix unitaire doit être supérieur à 0");
      }
      if (stockDisponible == null || stockDisponible < 0) {
        return ResponseEntity.badRequest().body("Le stock disponible ne peut pas être négatif");
      }

      Produit produit =
          Produit.builder()
              .nom(nom.trim())
              .categorie(categorie)
              .prixUnitaire(prixUnitaire)
              .stockDisponible(stockDisponible)
              .build();

      if (image != null && !image.isEmpty()) {
        // Validation de l'image
        if (!image.getContentType().startsWith("image/")) {
          return ResponseEntity.badRequest().body("Le fichier doit être une image");
        }
        if (image.getSize() > 5 * 1024 * 1024) { // 5MB max
          return ResponseEntity.badRequest().body("L'image ne doit pas dépasser 5MB");
        }
        produit.setImageData(image.getBytes());
        produit.setImageType(image.getContentType());
      }

      Produit savedProduit = produitService.save(produit);
      return ResponseEntity.ok(savedProduit);

    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur lors du traitement de l'image");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur interne du serveur");
    }
  }

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateProduit(
      @PathVariable Long id,
      @RequestParam("nom") String nom,
      @RequestParam("categorie") CategorieProduit categorie,
      @RequestParam("prixUnitaire") Double prixUnitaire,
      @RequestParam("stockDisponible") Integer stockDisponible,
      @RequestParam(value = "image", required = false) MultipartFile image) {

    try {
      // Validation des paramètres obligatoires
      if (nom == null || nom.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Le nom est obligatoire");
      }
      if (prixUnitaire == null || prixUnitaire <= 0) {
        return ResponseEntity.badRequest().body("Le prix unitaire doit être supérieur à 0");
      }
      if (stockDisponible == null || stockDisponible < 0) {
        return ResponseEntity.badRequest().body("Le stock disponible ne peut pas être négatif");
      }

      Produit produit =
          produitService
              .findById(id)
              .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + id));

      produit.setNom(nom.trim());
      produit.setCategorie(categorie);
      produit.setPrixUnitaire(prixUnitaire);
      produit.setStockDisponible(stockDisponible);

      // Gestion de l'image : seulement si une nouvelle image est fournie
      if (image != null && !image.isEmpty()) {
        // Validation de l'image
        if (!image.getContentType().startsWith("image/")) {
          return ResponseEntity.badRequest().body("Le fichier doit être une image");
        }
        if (image.getSize() > 5 * 1024 * 1024) { // 5MB max
          return ResponseEntity.badRequest().body("L'image ne doit pas dépasser 5MB");
        }
        produit.setImageData(image.getBytes());
        produit.setImageType(image.getContentType());
      }

      Produit updated = produitService.save(produit);
      return ResponseEntity.ok(updated);

    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur lors du traitement de l'image");
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur interne du serveur");
    }
  }

  @GetMapping("/{id}/image")
  public ResponseEntity<byte[]> getProduitImage(@PathVariable Long id) {
    try {
      return produitService
          .findById(id)
          .filter(p -> p.getImageData() != null && p.getImageType() != null)
          .map(
              p ->
                  ResponseEntity.ok()
                      .header(HttpHeaders.CONTENT_TYPE, p.getImageType())
                      .header(HttpHeaders.CACHE_CONTROL, "max-age=3600") // Cache pour 1 heure
                      .body(p.getImageData()))
          .orElse(ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
    try {
      produitService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // Endpoint pour supprimer seulement l'image d'un produit
  @DeleteMapping("/{id}/image")
  public ResponseEntity<?> removeProduitImage(@PathVariable Long id) {
    try {
      Produit produit =
          produitService
              .findById(id)
              .orElseThrow(() -> new RuntimeException("Produit introuvable"));

      produit.setImageData(null);
      produit.setImageType(null);
      produitService.save(produit);

      return ResponseEntity.ok().body("Image supprimée avec succès");
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erreur lors de la suppression de l'image");
    }
  }
}
