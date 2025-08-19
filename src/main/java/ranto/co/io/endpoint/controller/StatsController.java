package ranto.co.io.endpoint.controller;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.DepenseRepository;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

  private final DepenseRepository depenseRepository;
  private final CommandeRepository commandeRepository;

  public StatsController(
      DepenseRepository depenseRepository, CommandeRepository commandeRepository) {
    this.depenseRepository = depenseRepository;
    this.commandeRepository = commandeRepository;
  }

  // 🔹 Dépenses par jour
  @GetMapping("/depenses/jour")
  public Map<LocalDate, Double> getDepensesParJour() {
    Map<LocalDate, Double> result = new HashMap<>();
    depenseRepository
        .findAll()
        .forEach(
            depense -> {
              LocalDate date = depense.getDateDepense();
              result.merge(date, depense.getMontant(), Double::sum);
            });
    return result;
  }

  // 🔹 Dépenses par semaine
  @GetMapping("/depenses/semaine")
  public Map<Integer, Double> getDepensesParSemaine() {
    Map<Integer, Double> result = new HashMap<>();
    WeekFields wf = WeekFields.of(Locale.getDefault());
    depenseRepository
        .findAll()
        .forEach(
            depense -> {
              int week = depense.getDateDepense().get(wf.weekOfWeekBasedYear());
              result.merge(week, depense.getMontant(), Double::sum);
            });
    return result;
  }

  // 🔹 Dépenses par mois
  @GetMapping("/depenses/mois")
  public Map<String, Double> getDepensesParMois() {
    Map<String, Double> result = new HashMap<>();
    depenseRepository
        .findAll()
        .forEach(
            depense -> {
              String key =
                  depense.getDateDepense().getYear()
                      + "-"
                      + depense.getDateDepense().getMonthValue();
              result.merge(key, depense.getMontant(), Double::sum);
            });
    return result;
  }

  // 🔹 Revenus totaux sur une période
  @GetMapping("/revenus")
  public Double getRevenus(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

    return commandeRepository.findAll().stream()
        .filter(
            c ->
                !c.getDateCommande().toLocalDate().isBefore(debut)
                    && !c.getDateCommande().toLocalDate().isAfter(fin))
        .flatMap(c -> c.getDetails().stream())
        .mapToDouble(d -> d.getPrixTotal())
        .sum();
  }

  // 🔹 Bénéfice = Revenus - Dépenses
  @GetMapping("/benefice")
  public Double getBenefice(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

    double revenus = getRevenus(debut, fin);
    double depenses =
        depenseRepository.findAll().stream()
            .filter(d -> !d.getDateDepense().isBefore(debut) && !d.getDateDepense().isAfter(fin))
            .mapToDouble(d -> d.getMontant())
            .sum();
    return revenus - depenses;
  }
}
