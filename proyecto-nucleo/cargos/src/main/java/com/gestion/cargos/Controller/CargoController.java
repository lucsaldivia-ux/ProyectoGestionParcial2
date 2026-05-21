package com.gestion.cargos.Controller;

import com.gestion.cargos.DTO.CargoDTO;
import com.gestion.cargos.Model.Cargo;
import com.gestion.cargos.Service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cargos")
public class CargoController {

    @Autowired
    private CargoService service;

    @GetMapping
    public List<Cargo> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cargo> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crear(@RequestBody CargoDTO dto) {
        Boolean save = service.crearCargo(dto);
        if (!save) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Departamento no existe en su microservicio.");
        }
        return ResponseEntity.ok("creado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cargo> actualizar(@PathVariable Integer id, @RequestBody CargoDTO dto) {
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (service.eliminar(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
