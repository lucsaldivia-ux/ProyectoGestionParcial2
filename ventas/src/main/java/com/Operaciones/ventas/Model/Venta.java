package com.Operaciones.ventas.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private String producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double total;
    private LocalDateTime fecha;
}