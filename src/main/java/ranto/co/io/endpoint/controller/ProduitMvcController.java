package ranto.co.io.endpoint.controller;

import ranto.co.io.model.Produit;
import ranto.co.io.model.enums.CategorieProduit;
import ranto.co.io.service.ProduitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produits")
public class ProduitMvcController {

    private final ProduitService produitService;

    public ProduitMvcController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public String listeProduits(Model model) {
        model.addAttribute("produits", produitService.findAll());
        return "produits/liste";
    }

    @GetMapping("/new")
    public String formProduit(Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("categories", CategorieProduit.values());
        return "produits/form";
    }

    @GetMapping("/edit/{id}")
    public String editProduit(@PathVariable Long id, Model model) {
        var produit = produitService.findById(id).orElse(new Produit());
        model.addAttribute("produit", produit);
        model.addAttribute("categories", CategorieProduit.values());
        return "produits/form";
    }

    @PostMapping("/save")
    public String saveProduit(@ModelAttribute Produit produit) {
        produitService.save(produit);
        return "redirect:/produits";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduit(@PathVariable Long id) {
        produitService.delete(id);
        return "redirect:/produits";
    }
}
