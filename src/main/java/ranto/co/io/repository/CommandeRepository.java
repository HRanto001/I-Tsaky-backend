package ranto.co.io.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ranto.co.io.endpoint.controller.dto.TopProductDto;
import ranto.co.io.model.Commande;
import ranto.co.io.model.enums.StatutCommande;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

  List<Commande> findTop10ByOrderByDateCommandeDesc();

  List<Commande> findByStatut(StatutCommande statut);

    // Produits les plus vendus
    @Query("SELECT cd.produit.nom, SUM(cd.quantite) as totalVendu " +
            "FROM CommandeDetail cd " +
            "GROUP BY cd.produit.nom " +
            "ORDER BY totalVendu DESC")
    List<Object[]> findTopProducts();

    @Query("SELECT COUNT(c) FROM Commande c WHERE c.dateCommande >= :startDate")
    Long countOrders(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COALESCE(SUM(d.prixTotal), 0) FROM Commande c JOIN c.details d WHERE c.dateCommande >= :startDate")
    Double sumRevenue(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT new ranto.co.io.endpoint.controller.dto.TopProductDto(d.produit.nom, SUM(d.quantite), SUM(d.prixTotal)) " +
            "FROM Commande c JOIN c.details d " +
            "WHERE c.dateCommande >= :startDate " +
            "GROUP BY d.produit.nom " +
            "ORDER BY SUM(d.quantite) DESC")
    List<TopProductDto> findTopProducts(@Param("startDate") LocalDateTime startDate);
}
