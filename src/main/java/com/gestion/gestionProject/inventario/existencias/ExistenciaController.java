package com.gestion.gestionProject.inventario.existencias;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API del módulo de dominio <em>Inventario (existencias/movimientos)</em>.
 */
@RestController
@RequestMapping("/api/v1/inventario/existencias")
public class ExistenciaController {

    private final ExistenciaService service;

    public ExistenciaController(ExistenciaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Existencia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Existencia buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Existencia crear(@RequestBody Existencia existencia) {
        return service.crear(existencia);
    }

    @PutMapping("/{id}")
    public Existencia actualizar(@PathVariable Long id, @RequestBody Existencia existencia) {
        return service.actualizar(id, existencia);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
