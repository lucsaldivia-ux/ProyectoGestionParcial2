package com.gestion.ordenescompra.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class OrdenCompraDTO {
    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long proveedorId;

    private LocalDate fecha;

    @Size(max = 50)
    private String estado;

    @DecimalMin(value = "0.0")
    private Double total;
}
