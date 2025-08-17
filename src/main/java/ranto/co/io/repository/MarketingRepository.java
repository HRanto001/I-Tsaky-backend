package ranto.co.io.repository;

import ranto.co.io.model.Marketing;
import ranto.co.io.model.enums.CanalMarketing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MarketingRepository extends JpaRepository<Marketing, Long> {
    List<Marketing> findByCanal(CanalMarketing canal);
    List<Marketing> findByDateActionBetween(LocalDate debut, LocalDate fin);
}

