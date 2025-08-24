package ranto.co.io.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import ranto.co.io.model.Commande;
import ranto.co.io.model.Depense;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.DepenseRepository;

@Service
public class DashboardService {

  private final CommandeRepository commandeRepository;
  private final DepenseRepository depenseRepository;

  public DashboardService(
      CommandeRepository commandeRepository, DepenseRepository depenseRepository) {
    this.commandeRepository = commandeRepository;
    this.depenseRepository = depenseRepository;
  }

  public BigDecimal calculerBenefice() {
    // 1️⃣ Somme des commandes payées
    List<Commande> commandes = commandeRepository.findAll();
    BigDecimal totalCA =
        commandes.stream()
            .filter(c -> c.getStatut() != null && c.getStatut().name().equals("PAYEE"))
            .flatMap(c -> c.getDetails().stream())
            .map(
                cd ->
                    BigDecimal.valueOf(cd.getPrixTotal().doubleValue())) // convertir en BigDecimal
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    List<Depense> depenses = depenseRepository.findAll();
    BigDecimal totalDepenses =
        depenses.stream()
            .map(d -> BigDecimal.valueOf(d.getMontant().doubleValue())) // convertir en BigDecimal
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    // 3️⃣ Calcul bénéfice net
    return totalCA.subtract(totalDepenses);
  }
}
