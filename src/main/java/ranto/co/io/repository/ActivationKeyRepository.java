package ranto.co.io.repository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ranto.co.io.model.ActivationKey;

public interface ActivationKeyRepository extends JpaRepository<ActivationKey, Long> {
  Optional<ActivationKey> findByKeyValue(String keyValue);

  @Modifying
  @Transactional
  @Query(
      value =
          "UPDATE activation_keys "
              + "SET used = true "
              + "WHERE used = false AND expires_at < :now",
      nativeQuery = true)
  int markExpiredKeysAsUsed(LocalDateTime now);

  void deleteByExpiresAtBefore(LocalDateTime dateTime);

  boolean existsByKeyValue(String keyValue);
}
