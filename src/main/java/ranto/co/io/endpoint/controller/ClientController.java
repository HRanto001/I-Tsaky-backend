package ranto.co.io.endpoint.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Client;
import ranto.co.io.model.enums.TypeClient;
import ranto.co.io.repository.ClientRepository;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

  private final ClientRepository clientRepository;

  public ClientController(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @GetMapping
  public List<Client> getAllClients() {
    return clientRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Client> getClientById(@PathVariable Long id) {
    return clientRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/type/{type}")
  public List<Client> getByType(@PathVariable TypeClient type) {
    return clientRepository.findByTypeClient(type);
  }

  @PostMapping
  public Client createClient(@RequestBody Client client) {
    return clientRepository.save(client);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
    return clientRepository
        .findById(id)
        .map(
            existing -> {
              client.setId(existing.getId());
              return ResponseEntity.ok(clientRepository.save(client));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
    clientRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
