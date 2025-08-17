package ranto.co.io.model;

import jakarta.persistence.*;
import lombok.*;
import ranto.co.io.model.enums.CanalMarketing;

import java.time.LocalDate;

@Entity
@Table(name = "marketing")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Marketing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CanalMarketing canal;

    private Double cout;
    private LocalDate dateAction = LocalDate.now();
    private String description;
}
