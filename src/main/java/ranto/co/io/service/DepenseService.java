package ranto.co.io.service;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ranto.co.io.model.Depense;
import ranto.co.io.model.Utilisateur;
import ranto.co.io.repository.DepenseRepository;
import ranto.co.io.repository.UtilisateurRepository;

@Service
@AllArgsConstructor
public class DepenseService {

  private final UtilisateurRepository utilisateurRepository;
  private final DepenseRepository depenseRepository;

  public Depense save(Depense depense) {
    Utilisateur currentUser = getCurrentUser();
    depense.setCreatedBy(currentUser);
    return depenseRepository.save(depense);
  }

  public Depense update(Long id, Depense depense) {
    Utilisateur currentUser = getCurrentUser();

    return depenseRepository
        .findById(id)
        .map(
            existing -> {
              existing.setTypeDepense(depense.getTypeDepense());
              existing.setMontant(depense.getMontant());
              existing.setDescription(depense.getDescription());
              existing.setUpdatedBy(currentUser);
              existing.setUpdatedAt(LocalDateTime.now());
              return depenseRepository.save(existing);
            })
        .orElseThrow(() -> new RuntimeException("Dépense non trouvée avec l'id " + id));
  }

  private Utilisateur getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      String username = auth.getName();
      return utilisateurRepository
          .findByEmail(username)
          .orElseThrow(() -> new RuntimeException("Utilisateur introuvable: " + username));
    }
    throw new RuntimeException("Utilisateur non authentifié");
  }
}
