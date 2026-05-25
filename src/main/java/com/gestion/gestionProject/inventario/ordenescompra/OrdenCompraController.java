package com.gestion.gestionProject.inventario.ordenescompra;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario/ordenescompra")
public class OrdenCompraController {

    private final OrdenCompraService service;

    public OrdenCompraController(OrdenCompraService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrdenCompra> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public OrdenCompra buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenCompra crear(@RequestBody OrdenCompra orden) {
        return service.crear(orden);
    }

    @PutMapping("/{id}")
    public OrdenCompra actualizar(@PathVariable Long id, @RequestBody OrdenCompra orden) {
        return service.actualizar(id, orden);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
