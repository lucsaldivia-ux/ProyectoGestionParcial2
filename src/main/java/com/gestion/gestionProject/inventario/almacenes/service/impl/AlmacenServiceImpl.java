package com.gestion.gestionProject.inventario.almacenes.service.impl;

import com.gestion.gestionProject.inventario.almacenes.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    private final AlmacenRepository repo;

    public AlmacenServiceImpl(AlmacenRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Almacen> listar() {
        return repo.findAll();
    }

    @Override
    public Almacen buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Almacén no encontrado"));
    }

    @Override
    public Almacen crear(Almacen almacen) {
        return repo.save(almacen);
    }

    @Override
    public Almacen actualizar(Long id, Almacen datos) {
        Almacen existing = buscarPorId(id);
        existing.setNombre(datos.getNombre());
        existing.setUbicacion(datos.getUbicacion());
        return repo.save(existing);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        repo.deleteById(id);
    }
}
