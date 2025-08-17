package ranto.co.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.model.enums.Role;

import java.util.Optional;
import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findByRole(Role role);
    boolean existsByEmail(String email);
}

