package ranto.co.io.model;

import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ranto.co.io.repository.ActivationKeyRepository;

@Component
public class ActivationKeyCleanup {

  private final ActivationKeyRepository activationKeyRepository;

  public ActivationKeyCleanup(ActivationKeyRepository activationKeyRepository) {
    this.activationKeyRepository = activationKeyRepository;
  }

  // Tous les jours à minuit
  @Scheduled(cron = "0 0 0 * * ?")
  public void deleteExpiredKeys() {
    LocalDateTime now = LocalDateTime.now();
    activationKeyRepository.deleteByExpiresAtBefore(now);
  }
}
