package com.grupo.gestion.empleados.controller;
import com.grupo.gestion.empleados.dto.EmpleadoDTO;
import com.grupo.gestion.empleados.model.Empleado;
import com.grupo.gestion.empleados.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    @Autowired private EmpleadoService service;
    @GetMapping public List<Empleado> listar() { return service.obtenerTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Empleado> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Empleado crear(@RequestBody EmpleadoDTO dto) { return service.crear(dto); }
    @PutMapping("/{id}") public ResponseEntity<Empleado> actualizar(@PathVariable Integer id, @RequestBody EmpleadoDTO dto) {
        return service.actualizar(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (service.eliminar(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
