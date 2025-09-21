 package ranto.co.io.config;

 import lombok.RequiredArgsConstructor;
 import org.springframework.boot.CommandLineRunner;
 import org.springframework.stereotype.Component;
 import ranto.co.io.model.ActivationKey;
 import ranto.co.io.service.ActivationKeyService;

 @Component
 @RequiredArgsConstructor
 public class DataLoader implements CommandLineRunner {

  private final ActivationKeyService activationKeyService;

  @Override
  public void run(String... args) {
    // Exemple : clé fixe valable 7 jours
    activationKeyService.createKey("ABC123XYZ", 7);
  }
 }
