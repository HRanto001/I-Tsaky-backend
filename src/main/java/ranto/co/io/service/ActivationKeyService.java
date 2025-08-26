package ranto.co.io.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ranto.co.io.model.ActivationKey;
import ranto.co.io.repository.ActivationKeyRepository;

@Service
public class ActivationKeyService {

  private final ActivationKeyRepository activationKeyRepository;

  public ActivationKeyService(ActivationKeyRepository activationKeyRepository) {
    this.activationKeyRepository = activationKeyRepository;
  }

  // Génération d’une clé
  public ActivationKey generateKey() {
    ActivationKey key =
        ActivationKey.builder()
            .keyValue(UUID.randomUUID().toString())
            .used(false)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusHours(2))
            .build();

    return activationKeyRepository.save(key);
  }

  // Vérification et consommation
  public boolean useKey(String keyValue) {
    return activationKeyRepository
        .findByKeyValue(keyValue)
        .filter(key -> !key.isUsed())
        .filter(
            key -> key.getExpiresAt() == null || key.getExpiresAt().isAfter(LocalDateTime.now()))
        .map(
            key -> {
              key.setUsed(true); // marquer comme utilisé
              activationKeyRepository.save(key);
              return true;
            })
        .orElse(false);
  }
}
