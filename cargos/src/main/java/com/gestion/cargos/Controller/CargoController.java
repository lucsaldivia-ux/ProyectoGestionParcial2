package com.gestion.cargos.Controller;

import com.gestion.cargos.DTO.CargoDTO;
import com.gestion.cargos.Model.Cargo;
import com.gestion.cargos.Service.CargoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/cargos")
public class CargoController {

    @Autowired
    private CargoService service;

    @GetMapping
    public ResponseEntity<List<Cargo>> listar() {
        log.info("GET /api/v1/cargos - Listando todos los cargos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> obtener(@PathVariable Integer id) {
        log.info("GET /api/v1/cargos/{} - Buscando cargo", id);
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crear(@Valid @RequestBody CargoDTO dto) {
        log.info("POST /api/v1/cargos/crear - Creando cargo: {}", dto.getNombre());
        Boolean save = service.crearCargo(dto);
        if (!save) {
            log.warn("No se pudo crear el cargo: departamento no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Departamento no existe en su microservicio.");
        }
        return ResponseEntity.ok("creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> actualizar(@PathVariable Integer id, @Valid @RequestBody CargoDTO dto) {
        log.info("PUT /api/v1/cargos/{} - Actualizando cargo", id);
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/v1/cargos/{} - Desactivando cargo", id);
        if (service.eliminar(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
