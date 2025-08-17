package ranto.co.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Commande;
import ranto.co.io.model.enums.StatutCommande;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByStatut(StatutCommande statut);
    List<Commande> findByClientNomContainingIgnoreCase(String nomClient);
}

