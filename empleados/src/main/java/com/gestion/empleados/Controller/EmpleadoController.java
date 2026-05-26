package com.gestion.empleados.Controller;

import com.gestion.empleados.DTO.EmpleadoDTO;
import com.gestion.empleados.Model.Empleado;
import com.gestion.empleados.Service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService service;

    @GetMapping
    public ResponseEntity<List<Empleado>> listar() {
        log.info("GET /api/v1/empleados - Listando todos los empleados");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtener(@PathVariable Integer id) {
        log.info("GET /api/v1/empleados/{} - Buscando empleado", id);
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crear(@Valid @RequestBody EmpleadoDTO dto) {
        log.info("POST /api/v1/empleados/crear - Creando empleado: {}", dto.getNombre());
        Boolean save = service.crearEmpleado(dto);
        if (!save) {
            log.warn("No se pudo crear el empleado: departamento o cargo no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Departamento o cargo no existen en sus respectivos microservicios.");
        }
        return ResponseEntity.ok("creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Integer id, @Valid @RequestBody EmpleadoDTO dto) {
        log.info("PUT /api/v1/empleados/{} - Actualizando empleado", id);
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/v1/empleados/{} - Desactivando empleado", id);
        if (service.eliminar(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
