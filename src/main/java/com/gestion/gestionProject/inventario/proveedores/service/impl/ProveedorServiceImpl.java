package com.gestion.gestionProject.inventario.proveedores.service.impl;

import com.gestion.gestionProject.inventario.proveedores.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repo;

    public ProveedorServiceImpl(ProveedorRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Proveedor> listar() {
        return repo.findAll();
    }

    @Override
    public Proveedor buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
    }

    @Override
    public Proveedor crear(Proveedor proveedor) {
        return repo.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Long id, Proveedor datos) {
        Proveedor existing = buscarPorId(id);
        existing.setNombre(datos.getNombre());
        existing.setNif(datos.getNif());
        return repo.save(existing);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        repo.deleteById(id);
    }
}
