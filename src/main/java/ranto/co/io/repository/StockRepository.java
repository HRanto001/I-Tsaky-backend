package ranto.co.io.repository;

import ranto.co.io.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByNomMatiereContainingIgnoreCase(String nom);
    List<Stock> findByQuantiteLessThanEqual(Integer seuil);
}
