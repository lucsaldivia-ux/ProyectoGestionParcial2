package com.gestion.empleados.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpleadoDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener formato valido")
    private String email;

    @Size(max = 20)
    private String telefono;

    @NotNull(message = "El ID del departamento es obligatorio")
    private Integer departamentoId;

    @NotNull(message = "El ID del cargo es obligatorio")
    private Integer cargoId;
}
