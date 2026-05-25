package com.gestion.gestionProject.inventario.existencias;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "existencias")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Existencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referencia;

    @Column(precision = 19, scale = 2)
    private BigDecimal cantidad;
}
