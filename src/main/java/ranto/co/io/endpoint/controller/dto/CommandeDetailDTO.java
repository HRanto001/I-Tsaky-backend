package ranto.co.io.endpoint.controller.dto;

import lombok.Data;
import ranto.co.io.model.CommandeDetail;

@Data
public class CommandeDetailDTO {
    private Long id;
    private ProduitDTO produit;
    private Integer quantite;
    private Double prixTotal;

    public CommandeDetailDTO(CommandeDetail detail) {
        this.id = detail.getId();
        this.produit = new ProduitDTO(detail.getProduit());
        this.quantite = detail.getQuantite();
        this.prixTotal = detail.getPrixTotal();
    }

    // Getters et Setters
}
