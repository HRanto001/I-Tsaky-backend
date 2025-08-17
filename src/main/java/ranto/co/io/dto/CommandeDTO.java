package ranto.co.io.dto;

import ranto.co.io.model.enums.StatutCommande;

import java.time.LocalDateTime;

public record CommandeDTO(
        Long id,
        String clientNom,
        LocalDateTime dateCommande,
        StatutCommande statut,
        Double montantTotal
) {}
