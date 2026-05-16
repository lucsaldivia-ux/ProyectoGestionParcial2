package com.grupo.gestion.departamentos.controller;
import com.grupo.gestion.departamentos.dto.DepartamentoDTO;
import com.grupo.gestion.departamentos.model.Departamento;
import com.grupo.gestion.departamentos.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/departamentos")
public class DepartamentoController {
    @Autowired private DepartamentoService service;
    @GetMapping public List<Departamento> listar() { return service.obtenerTodos(); }
    @GetMapping("/{id}") public ResponseEntity<Departamento> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Departamento crear(@RequestBody DepartamentoDTO dto) { return service.crear(dto); }
    @PutMapping("/{id}") public ResponseEntity<Departamento> actualizar(@PathVariable Integer id, @RequestBody DepartamentoDTO dto) {
        return service.actualizar(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (service.eliminar(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
