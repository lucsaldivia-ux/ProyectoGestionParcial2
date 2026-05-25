package com.gestion.gestionProject.inventario.existencias.service.impl;

import com.gestion.gestionProject.inventario.existencias.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExistenciaServiceImpl implements ExistenciaService {

    private final ExistenciaRepository repo;

    public ExistenciaServiceImpl(ExistenciaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Existencia> listar() {
        return repo.findAll();
    }

    @Override
    public Existencia buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Existencia no encontrada"));
    }

    @Override
    public Existencia crear(Existencia existencia) {
        return repo.save(existencia);
    }

    @Override
    public Existencia actualizar(Long id, Existencia datos) {
        Existencia existing = buscarPorId(id);
        existing.setReferencia(datos.getReferencia());
        existing.setCantidad(datos.getCantidad());
        return repo.save(existing);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        repo.deleteById(id);
    }
}
