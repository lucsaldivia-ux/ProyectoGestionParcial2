package com.gestion.auditoria.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuditoriaDTO {
    @NotBlank(message = "La accion es obligatoria")
    @Size(max = 100)
    private String accion;

    @NotBlank(message = "La tabla es obligatoria")
    @Size(max = 100)
    private String tabla;

    private Integer registroId;

    @Size(max = 100)
    private String usuario;

    @Size(max = 500)
    private String detalles;
}
