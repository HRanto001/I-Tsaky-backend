
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ActivationKeyService activationKeyService;

    @Override
    public void run(String... args) throws Exception {
        // Crée une clé ABC123XYZ valide 7 jours
        activationKeyService.createKey("ABC123XYZ", 7);
        System.out.println("Activation key inserted!");
    }
}
