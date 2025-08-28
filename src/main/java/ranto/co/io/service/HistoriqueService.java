package ranto.co.io.service;

import org.springframework.stereotype.Service;
import ranto.co.io.model.Historique;
import ranto.co.io.repository.HistoriqueRepository;

import java.time.LocalDateTime;

@Service
public class HistoriqueService {
    private final HistoriqueRepository repo;

    public HistoriqueService(HistoriqueRepository repo) {
        this.repo = repo;
    }

    public void enregistrer(String methode, String endpoint, String utilisateur, String payload) {
        Historique h = new Historique();
        h.setMethode(methode);
        h.setEndpoint(endpoint);
        h.setUtilisateur(utilisateur);
        h.setPayload(payload);
        h.setDateAction(LocalDateTime.now());
        repo.save(h);
    }
}

