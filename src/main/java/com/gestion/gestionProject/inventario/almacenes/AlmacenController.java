package com.gestion.gestionProject.inventario.almacenes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario/almacenes")
public class AlmacenController {

    private final AlmacenService service;

    public AlmacenController(AlmacenService service) {
        this.service = service;
    }

    @GetMapping
    public List<Almacen> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Almacen buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Almacen crear(@RequestBody Almacen almacen) {
        return service.crear(almacen);
    }

    @PutMapping("/{id}")
    public Almacen actualizar(@PathVariable Long id, @RequestBody Almacen almacen) {
        return service.actualizar(id, almacen);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
