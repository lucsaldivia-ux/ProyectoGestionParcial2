package com.Operaciones.Pagos.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ventaId;
    private Double monto;
    private String metodoPago;
    private String estado;
    private LocalDateTime fecha;
}