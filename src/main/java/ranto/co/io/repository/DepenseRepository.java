package ranto.co.io.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Depense;
import ranto.co.io.model.enums.TypeDepense;

public interface DepenseRepository extends JpaRepository<Depense, Long> {
  List<Depense> findByTypeDepense(TypeDepense typeDepense);

  List<Depense> findByDateDepenseBetween(LocalDate debut, LocalDate fin);
}
