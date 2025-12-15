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

  public ActivationKeyController(
      ActivationKeyService activationKeyService, ActivationKeyRepository activationKeyRepository) {
    this.activationKeyService = activationKeyService;
    this.activationKeyRepository = activationKeyRepository;
  }

  @GetMapping
  public List<ActivationKey> getAllKeys() {
    return activationKeyService.getAllKeys();
  }

  @PostMapping("/generate")
  public ActivationKey generateKey() {
    return activationKeyService.generateKey();
  }

  @PostMapping("/generate-and-send")
  public ActivationKey generateAndSend(@RequestParam String email) {
    return activationKeyService.generateAndSendActivationKey(email);
  }

  @PostMapping("/generate-reset")
  public ActivationKey generateResetKey(@RequestParam String email) {
    return activationKeyService.generateAndSendResetKey(email);
  }

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
