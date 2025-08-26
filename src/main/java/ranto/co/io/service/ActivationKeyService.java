package ranto.co.io.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import ranto.co.io.model.ActivationKey;
import ranto.co.io.repository.ActivationKeyRepository;

@Service
public class ActivationKeyService {

  private final ActivationKeyRepository activationKeyRepository;

  public ActivationKeyService(ActivationKeyRepository activationKeyRepository) {
    this.activationKeyRepository = activationKeyRepository;
  }

  public List<ActivationKey> getAllKeys() {
    return activationKeyRepository.findAll();
  }

  public ActivationKey generateKey() {
    // Générer une clé alphanumérique de 8 caractères
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder keyValue = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 8; i++) {
      keyValue.append(chars.charAt(random.nextInt(chars.length())));
    }

    ActivationKey key =
        ActivationKey.builder()
            .keyValue(keyValue.toString())
            .used(false)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusDays(1))
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
