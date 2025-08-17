package ranto.co.io.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.CommandeDetail;

import java.util.List;

public interface CommandeDetailRepository extends JpaRepository<CommandeDetail, Long> {
    List<CommandeDetail> findByCommandeId(Long commandeId);
    List<CommandeDetail> findByProduitId(Long produitId);
}
