package ranto.co.io.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.ActivationKey;

public interface ActivationKeyRepository extends JpaRepository<ActivationKey, Long> {
  Optional<ActivationKey> findByKeyValue(String keyValue);
}
