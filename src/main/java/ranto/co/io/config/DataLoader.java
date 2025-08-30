package ranto.co.io.config;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ActivationKeyService activationKeyService;

    @Override
    public void run(String... args) {
        // Exemple : clé fixe valable 7 jours
        activationKeyService.createKey("ABC123XYZ", 7);

        // Ou bien générer une clé aléatoire
        ActivationKey randomKey = activationKeyService.generateKey();
        System.out.println("Generated activation key: " + randomKey.getKeyValue());
    }
}
