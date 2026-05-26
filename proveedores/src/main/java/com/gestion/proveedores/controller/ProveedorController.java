package com.gestion.proveedores.controller;

import com.gestion.proveedores.dto.ProveedorDTO;
import com.gestion.proveedores.model.Proveedor;
import com.gestion.proveedores.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> listar() {
        log.info("GET /api/v1/proveedores");
        return ResponseEntity.ok(proveedorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/proveedores/{}", id);
        return ResponseEntity.ok(proveedorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@Valid @RequestBody ProveedorDTO dto) {
        log.info("POST /api/v1/proveedores - nombre: {}", dto.getNombre());
        return ResponseEntity.status(201).body(proveedorService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @Valid @RequestBody ProveedorDTO dto) {
        log.info("PUT /api/v1/proveedores/{}", id);
        return ResponseEntity.ok(proveedorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /api/v1/proveedores/{}", id);
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
