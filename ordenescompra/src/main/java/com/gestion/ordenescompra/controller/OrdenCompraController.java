package com.gestion.ordenescompra.controller;

import com.gestion.ordenescompra.dto.OrdenCompraDTO;
import com.gestion.ordenescompra.model.OrdenCompra;
import com.gestion.ordenescompra.service.OrdenCompraService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/ordenes-compra")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    @GetMapping
    public ResponseEntity<List<OrdenCompra>> listar() {
        log.info("GET /api/v1/ordenes-compra");
        return ResponseEntity.ok(ordenCompraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/ordenes-compra/{}", id);
        return ResponseEntity.ok(ordenCompraService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrdenCompra> crear(@Valid @RequestBody OrdenCompraDTO dto) {
        log.info("POST /api/v1/ordenes-compra - proveedorId: {}", dto.getProveedorId());
        return ResponseEntity.status(201).body(ordenCompraService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompra> actualizar(@PathVariable Long id, @Valid @RequestBody OrdenCompraDTO dto) {
        log.info("PUT /api/v1/ordenes-compra/{}", id);
        return ResponseEntity.ok(ordenCompraService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /api/v1/ordenes-compra/{}", id);
        ordenCompraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
