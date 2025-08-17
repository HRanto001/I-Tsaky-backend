package ranto.co.io.endpoint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Client;
import ranto.co.io.model.enums.TypeClient;
import ranto.co.io.repository.ClientRepository;

@Controller
@RequestMapping("/clients")
public class ClientMvcController {

  private final ClientRepository clientRepository;

  public ClientMvcController(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @GetMapping
  public String listeClients(Model model) {
    model.addAttribute("clients", clientRepository.findAll());
    return "clients/liste";
  }

  @GetMapping("/new")
  public String formClient(Model model) {
    model.addAttribute("client", new Client());
    model.addAttribute("types", TypeClient.values());
    return "clients/form";
  }

  @GetMapping("/edit/{id}")
  public String editClient(@PathVariable Long id, Model model) {
    var client = clientRepository.findById(id).orElse(new Client());
    model.addAttribute("client", client);
    model.addAttribute("types", TypeClient.values());
    return "clients/form";
  }

  @PostMapping("/save")
  public String saveClient(@ModelAttribute Client client) {
    clientRepository.save(client);
    return "redirect:/clients";
  }

  @GetMapping("/delete/{id}")
  public String deleteClient(@PathVariable Long id) {
    clientRepository.deleteById(id);
    return "redirect:/clients";
  }
}
