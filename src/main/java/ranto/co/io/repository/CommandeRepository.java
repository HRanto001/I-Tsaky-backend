package ranto.co.io.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ranto.co.io.endpoint.controller.dto.TopProductDto;
import ranto.co.io.model.Commande;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.StatutCommande;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

  List<Commande> findTop10ByOrderByDateCommandeDesc();

  List<Commande> findTop10ByCreatedByOrderByDateCommandeDesc(Utilisateur user);

  List<Commande> findByStatut(StatutCommande statut);

  List<Commande> findByCreatedBy(Utilisateur user);

  Page<Commande> findByCreatedBy(Utilisateur user, Pageable pageable);

  Page<Commande> findAllByOrderByDateCommandeDesc(Pageable pageable);

  // Pagination pour un utilisateur précis (tri date desc)
  Page<Commande> findByCreatedByOrderByDateCommandeDesc(Utilisateur user, Pageable pageable);

  // Produits les plus vendus
  @Query(
      "SELECT cd.produit.nom, SUM(cd.quantite) as totalVendu "
          + "FROM CommandeDetail cd "
          + "GROUP BY cd.produit.nom "
          + "ORDER BY totalVendu DESC")
  List<Object[]> findTopProducts();

  @Query(
      "SELECT COUNT(c) FROM Commande c WHERE c.dateCommande >= :startDate AND c.statut <>"
          + " 'ANNULEE'")
  Long countOrders(@Param("startDate") LocalDateTime startDate);

  @Query(
      "SELECT COALESCE(SUM(d.prixTotal), 0) "
          + "FROM Commande c JOIN c.details d "
          + "WHERE c.dateCommande >= :startDate "
          + "AND c.statut <> 'ANNULEE'")
  Double sumRevenue(@Param("startDate") LocalDateTime startDate);

  @Query(
      "SELECT new ranto.co.io.endpoint.controller.dto.TopProductDto(d.produit.nom, SUM(d.quantite),"
          + " SUM(d.prixTotal)) FROM Commande c JOIN c.details d WHERE c.dateCommande >= :startDate"
          + " AND c.statut <> 'ANNULEE' GROUP BY d.produit.nom ORDER BY SUM(d.quantite) DESC")
  List<TopProductDto> findTopProducts(@Param("startDate") LocalDateTime startDate);

  // Somme entre deux dates
  @Query(
      "SELECT COALESCE(SUM(d.prixTotal), 0) "
          + "FROM Commande c JOIN c.details d "
          + "WHERE c.dateCommande >= :startDate AND c.dateCommande <= :endDate "
          + "AND c.statut <> 'ANNULEE'")
  Double sumRevenueBetween(
      @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    long countByCreatedBy(Utilisateur user);
}
