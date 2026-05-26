package com.gestion.almacenes.controller;

import com.gestion.almacenes.dto.AlmacenDTO;
import com.gestion.almacenes.model.Almacen;
import com.gestion.almacenes.service.AlmacenService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping
    public ResponseEntity<List<Almacen>> listar() {
        log.info("GET /api/v1/almacenes");
        return ResponseEntity.ok(almacenService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Almacen> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/almacenes/{}", id);
        return ResponseEntity.ok(almacenService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Almacen> crear(@Valid @RequestBody AlmacenDTO dto) {
        log.info("POST /api/v1/almacenes - nombre: {}", dto.getNombre());
        return ResponseEntity.status(201).body(almacenService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizar(@PathVariable Long id, @Valid @RequestBody AlmacenDTO dto) {
        log.info("PUT /api/v1/almacenes/{}", id);
        return ResponseEntity.ok(almacenService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /api/v1/almacenes/{}", id);
        almacenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
