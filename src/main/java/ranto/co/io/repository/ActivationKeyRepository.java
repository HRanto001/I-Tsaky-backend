package ranto.co.io.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.ActivationKey;

public interface ActivationKeyRepository extends JpaRepository<ActivationKey, Long> {
  Optional<ActivationKey> findByKeyValue(String keyValue);

  void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
