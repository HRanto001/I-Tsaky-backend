package ranto.co.io.model;

import jakarta.persistence.*;
import lombok.*;
import ranto.co.io.model.enums.UniteStock;

@Entity
@Table(name = "stocks")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomMatiere;

    private Integer quantite;

    @Enumerated(EnumType.STRING)
    private UniteStock unite;

    private Integer seuilAlerte;
}

