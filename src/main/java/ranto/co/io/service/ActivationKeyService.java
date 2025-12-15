package ranto.co.io.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ranto.co.io.model.ActivationKey;
import ranto.co.io.repository.ActivationKeyRepository;

@AllArgsConstructor
@Service
public class ActivationKeyService {

  private final ActivationKeyRepository activationKeyRepository;
  private final EmailTemplateService emailTemplateService;

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

  public ActivationKey createKey(String keyValue, int validDays) {
    ActivationKey key =
        ActivationKey.builder()
            .keyValue(keyValue)
            .used(false)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusDays(validDays))
            .build();

    return activationKeyRepository.save(key);
  }

  // Vérification et consommation
  public boolean useKey(String keyValue) {
    return activationKeyRepository
        .findByKeyValue(keyValue)
        .map(
            key -> {
              // Si déjà utilisée
              if (key.isUsed()) {
                return false;
              }

              // Vérifier expiration
              if (key.getExpiresAt() != null && key.getExpiresAt().isBefore(LocalDateTime.now())) {
                key.setUsed(true); // marquer comme utilisée car expirée
                activationKeyRepository.save(key);
                return false; // car on ne peut pas l'utiliser
              }

              // Sinon marquer comme utilisée normalement
              key.setUsed(true);
              activationKeyRepository.save(key);
              return true;
            })
        .orElse(false);
  }

  public boolean validateKeyForReset(String keyValue) {
    return activationKeyRepository
        .findByKeyValue(keyValue)
        .filter(
            key -> key.getExpiresAt() == null || key.getExpiresAt().isAfter(LocalDateTime.now()))
        .isPresent();
  }

  public Optional<ActivationKey> findByValue(String value) {
    return activationKeyRepository.findByKeyValue(value);
  }

  public ActivationKey save(ActivationKey key) {
    return activationKeyRepository.save(key);
  }

  @Scheduled(cron = "0 0 0 * * *") // tous les jours à minuit
  @Transactional
  public void markExpiredKeysAsUsed() {
    int updated = activationKeyRepository.markExpiredKeysAsUsed(LocalDateTime.now());
    if (updated > 0) {
      System.out.println("⚡ " + updated + " clés expirées ont été marquées comme utilisées.");
    }
  }

  public ActivationKey generateAndSendActivationKey(String email) {

    ActivationKey key = generateKey();

    emailTemplateService.sendAccountActivationEmail(email, key.getKeyValue());

    return key;
  }

  public ActivationKey generateAndSendResetKey(String email) {

    ActivationKey key = generateKey();

    key.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    activationKeyRepository.save(key);

    emailTemplateService.sendPasswordResetEmail(email, key.getKeyValue());

    return key;
  }
}
