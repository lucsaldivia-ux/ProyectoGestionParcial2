package com.gestion.gestionProject.inventario.ordenescompra;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ordenes_compra")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    private String estado;
}
