package ranto.co.io.repository;

import ranto.co.io.model.Depense;
import ranto.co.io.model.enums.TypeDepense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DepenseRepository extends JpaRepository<Depense, Long> {
    List<Depense> findByTypeDepense(TypeDepense typeDepense);
    List<Depense> findByDateDepenseBetween(LocalDate debut, LocalDate fin);
}

