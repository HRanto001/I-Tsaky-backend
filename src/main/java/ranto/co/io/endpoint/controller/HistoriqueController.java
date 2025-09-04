package ranto.co.io.endpoint.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Historique;
import ranto.co.io.repository.HistoriqueRepository;

@RestController
@RequestMapping("/api/historiques")
public class HistoriqueController {

  private final HistoriqueRepository historiqueRepository;

  public HistoriqueController(HistoriqueRepository historiqueRepository) {
    this.historiqueRepository = historiqueRepository;
  }

  @GetMapping
  public Page<Historique> getHistoriques(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("dateAction").descending());
    return historiqueRepository.findAll(pageable);
  }
}
