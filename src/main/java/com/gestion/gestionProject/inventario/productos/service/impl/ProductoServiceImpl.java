package com.gestion.gestionProject.inventario.productos.service.impl;

import com.gestion.gestionProject.inventario.productos.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;

    public ProductoServiceImpl(ProductoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Producto> listar() {
        return repo.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    @Override
    public Producto crear(Producto producto) {
        return repo.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto datos) {
        Producto existing = buscarPorId(id);
        existing.setNombre(datos.getNombre());
        existing.setCodigo(datos.getCodigo());
        return repo.save(existing);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        repo.deleteById(id);
    }
}
