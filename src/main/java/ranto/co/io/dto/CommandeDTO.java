package ranto.co.io.dto;

import java.time.LocalDateTime;
import ranto.co.io.model.enums.StatutCommande;

public record CommandeDTO(
    Long id,
    String clientNom,
    LocalDateTime dateCommande,
    StatutCommande statut,
    Double montantTotal) {}
