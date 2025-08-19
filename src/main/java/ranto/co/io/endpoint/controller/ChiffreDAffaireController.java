package ranto.co.io.endpoint.controller;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ranto.co.io.repository.CommandeRepository;

@RestController
public class ChiffreDAffaireController {
  private final CommandeRepository commandeRepository;

  public ChiffreDAffaireController(CommandeRepository commandeRepository) {
    this.commandeRepository = commandeRepository;
  }

  @GetMapping("/chiffre-affaires")
  public Double getChiffreAffaires(
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
}
