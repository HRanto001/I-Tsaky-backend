package ranto.co.io.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ranto.co.io.model.Depense;
import ranto.co.io.model.enums.TypeDepense;

public interface DepenseRepository extends JpaRepository<Depense, Long> {
  List<Depense> findByTypeDepense(TypeDepense typeDepense);

  List<Depense> findByDateDepenseBetween(LocalDate debut, LocalDate fin);

    @Query("SELECT SUM(d.montant) FROM Depense d WHERE d.dateDepense >= :startDate")
    Double sumExpenses(@Param("startDate") LocalDate startDate);

    @Query("SELECT CONCAT(d.typeDepense, ': ', SUM(d.montant), ' Ar') " +
            "FROM Depense d WHERE d.dateDepense >= :startDate " +
            "GROUP BY d.typeDepense ORDER BY SUM(d.montant) DESC LIMIT 1")
    String findMainExpense(@Param("startDate") LocalDate startDate);
}
