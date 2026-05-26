package com.gestion.ordenescompra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "ordenes_compra")
public class OrdenCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long proveedorId;

    private LocalDate fecha;

    @Size(max = 50, message = "El estado no puede superar 50 caracteres")
    private String estado;

    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    private Double total;
}
