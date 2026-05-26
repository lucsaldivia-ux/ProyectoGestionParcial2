package com.gestion.autenticacion.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
    @NotBlank(message = "El username es obligatorio")
    @Size(max = 100)
    private String username;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;

    @Size(max = 50)
    private String rol;

    @NotNull(message = "El ID del empleado es obligatorio")
    private Integer empleadoId;
}
