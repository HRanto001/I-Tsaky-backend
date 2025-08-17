package ranto.co.io.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
  List<Stock> findByNomMatiereContainingIgnoreCase(String nom);

  List<Stock> findByQuantiteLessThanEqual(Integer seuil);
}
