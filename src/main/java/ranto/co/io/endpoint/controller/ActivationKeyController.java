package ranto.co.io.endpoint.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.ActivationKey;
import ranto.co.io.repository.ActivationKeyRepository;
import ranto.co.io.service.ActivationKeyService;

@RestController
@RequestMapping("/api/activation-keys")
public class ActivationKeyController {

  private final ActivationKeyService activationKeyService;
  private final ActivationKeyRepository activationKeyRepository;

  public ActivationKeyController(ActivationKeyService activationKeyService, ActivationKeyRepository activationKeyRepository) {
    this.activationKeyService = activationKeyService;
      this.activationKeyRepository = activationKeyRepository;
  }

  // Récupérer toutes les clés
  @GetMapping
  public List<ActivationKey> getAllKeys() {
    return activationKeyService.getAllKeys();
  }

  // Générer une nouvelle clé
  @PostMapping("/generate")
  public ActivationKey generateKey() {
    return activationKeyService.generateKey();
  }

  // Consommer une clé (utilisée lors du register user)
  @PostMapping("/use/{key}")
  public boolean useKey(@PathVariable String key) {
    return activationKeyService.useKey(key);
  }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        activationKeyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
