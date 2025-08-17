package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Client;
import ranto.co.io.model.enums.TypeClient;
import ranto.co.io.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  // 🔹 Récupérer tous les clients
  @GetMapping
  public List<Client> getAllClients() {
    return clientService.findAll();
  }

  // 🔹 Récupérer un client par ID
  @GetMapping("/{id}")
  public ResponseEntity<Client> getClientById(@PathVariable Long id) {
    return clientService
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // 🔹 Récupérer les clients par type
  @GetMapping("/type/{typeClient}")
  public List<Client> getClientsByType(@PathVariable TypeClient typeClient) {
    return clientService.findByTypeClient(typeClient);
  }

  // 🔹 Rechercher des clients par nom (contient)
  @GetMapping("/search")
  public List<Client> searchClients(@RequestParam String nom) {
    return clientService.findByNomContaining(nom);
  }

  // 🔹 Créer un client
  @PostMapping
  public Client createClient(@RequestBody Client client) {
    return clientService.save(client);
  }

  // 🔹 Mettre à jour un client
  @PutMapping("/{id}")
  public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
    return clientService
        .findById(id)
        .map(
            existing -> {
              client.setId(existing.getId());
              return ResponseEntity.ok(clientService.save(client));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  // 🔹 Supprimer un client
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
    clientService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
