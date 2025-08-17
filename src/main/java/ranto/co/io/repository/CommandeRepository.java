package ranto.co.io.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Commande;
import ranto.co.io.model.enums.StatutCommande;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
  List<Commande> findByStatut(StatutCommande statut);

  List<Commande> findByClientNomContainingIgnoreCase(String nomClient);
}
