package ranto.co.io.endpoint.controller;

import org.springframework.web.bind.annotation.*;
import ranto.co.io.model.Historique;
import ranto.co.io.repository.HistoriqueRepository;

import java.util.List;

@RestController
@RequestMapping("/api/historiques")
public class HistoriqueController {

    private final HistoriqueRepository historiqueRepository;

    public HistoriqueController(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    @GetMapping
    public List<Historique> getAllHistoriques() {
        return historiqueRepository.findAll();
    }
}

