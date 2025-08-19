package ranto.co.io.endpoint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Commande;
import ranto.co.io.model.enums.StatutCommande;
import ranto.co.io.service.ClientService;
import ranto.co.io.service.CommandeService;
import ranto.co.io.service.ProduitService;

@Controller
@RequestMapping("/commandes")
public class CommandeMvcController {

  private final CommandeService commandeService;
  private final ClientService clientService;
  private final ProduitService produitService;

  public CommandeMvcController(
      CommandeService commandeService, ClientService clientService, ProduitService produitService) {
    this.commandeService = commandeService;
    this.clientService = clientService;
    this.produitService = produitService;
  }

  @GetMapping
  public String listeCommandes(Model model) {
    model.addAttribute("commandes", commandeService.findAll());
    return "commandes/liste"; // Thymeleaf view
  }

  @GetMapping("/new")
  public String formCommande(Model model) {
    model.addAttribute("clients", clientService.findAll());
    model.addAttribute("produits", produitService.findAll());
    model.addAttribute("statuts", StatutCommande.values());
    model.addAttribute("commande", new Commande());
    return "commandes/form"; // le template Thymeleaf
  }

  @GetMapping("/edit/{id}")
  public String editCommande(@PathVariable Long id, Model model) {
    var commande = commandeService.findById(id).orElse(new Commande());
    model.addAttribute("commande", commande);
    model.addAttribute("clients", clientService.findAll());
    model.addAttribute("produits", produitService.findAll());
    model.addAttribute("statuts", StatutCommande.values());
    return "commandes/form";
  }

  @PostMapping("/save")
  public String saveCommande(@ModelAttribute Commande commande) {
    commandeService.save(commande);
    return "redirect:/commandes";
  }

  @GetMapping("/delete/{id}")
  public String deleteCommande(@PathVariable Long id) {
    commandeService.delete(id);
    return "redirect:/commandes";
  }
}
