package ranto.co.io.endpoint.controller;

import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.ActivationKey;
import ranto.co.io.service.ActivationKeyService;

@RestController
@RequestMapping("/api/activation-keys")
public class ActivationKeyController {

  private final ActivationKeyService activationKeyService;

  public ActivationKeyController(ActivationKeyService activationKeyService) {
    this.activationKeyService = activationKeyService;
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
}
