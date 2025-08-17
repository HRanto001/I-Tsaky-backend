package ranto.co.io.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.CommandeDetail;

public interface CommandeDetailRepository extends JpaRepository<CommandeDetail, Long> {
  List<CommandeDetail> findByCommandeId(Long commandeId);

  List<CommandeDetail> findByProduitId(Long produitId);
}
