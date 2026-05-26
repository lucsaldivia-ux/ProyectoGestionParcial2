package com.gestion.existencias.controller;

import com.gestion.existencias.dto.ExistenciaDTO;
import com.gestion.existencias.model.Existencia;
import com.gestion.existencias.service.ExistenciaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/existencias")
public class ExistenciaController {

    private final ExistenciaService existenciaService;

    public ExistenciaController(ExistenciaService existenciaService) {
        this.existenciaService = existenciaService;
    }

    @GetMapping
    public ResponseEntity<List<Existencia>> listar() {
        log.info("GET /api/v1/existencias");
        return ResponseEntity.ok(existenciaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Existencia> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/existencias/{}", id);
        return ResponseEntity.ok(existenciaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Existencia> crear(@Valid @RequestBody ExistenciaDTO dto) {
        log.info("POST /api/v1/existencias - productoId: {}", dto.getProductoId());
        return ResponseEntity.status(201).body(existenciaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Existencia> actualizar(@PathVariable Long id, @Valid @RequestBody ExistenciaDTO dto) {
        log.info("PUT /api/v1/existencias/{}", id);
        return ResponseEntity.ok(existenciaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /api/v1/existencias/{}", id);
        existenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
