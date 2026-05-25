package com.gestion.gestionProject.inventario.almacenes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "almacenes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String ubicacion;
}
