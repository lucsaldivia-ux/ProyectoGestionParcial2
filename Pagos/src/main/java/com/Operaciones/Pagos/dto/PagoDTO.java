package com.Operaciones.Pagos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagoDTO {

    @NotNull(message = "El ventaId es obligatorio")
    private Long ventaId;

    @NotNull(message = "El monto es obligatorio")
    private Double monto;

    @NotBlank(message = "El metodo de pago es obligatorio")
    private String metodoPago;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
