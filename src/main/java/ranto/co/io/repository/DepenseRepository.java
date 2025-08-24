package ranto.co.io.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ranto.co.io.model.Depense;
import ranto.co.io.model.enums.TypeDepense;

public interface DepenseRepository extends JpaRepository<Depense, Long> {
  List<Depense> findByTypeDepense(TypeDepense typeDepense);

  List<Depense> findByDateDepenseBetween(LocalDate debut, LocalDate fin);

    // Somme des dépenses
    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d WHERE d.dateDepense >= :startDate")
    Double sumExpenses(LocalDate startDate);
}
