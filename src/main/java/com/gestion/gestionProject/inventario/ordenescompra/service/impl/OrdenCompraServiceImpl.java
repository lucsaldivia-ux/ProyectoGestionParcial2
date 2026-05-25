package com.gestion.gestionProject.inventario.ordenescompra.service.impl;

import com.gestion.gestionProject.inventario.ordenescompra.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository repo;

    public OrdenCompraServiceImpl(OrdenCompraRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<OrdenCompra> listar() {
        return repo.findAll();
    }

    @Override
    public OrdenCompra buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden de compra no encontrada"));
    }

    @Override
    public OrdenCompra crear(OrdenCompra orden) {
        return repo.save(orden);
    }

    @Override
    public OrdenCompra actualizar(Long id, OrdenCompra datos) {
        OrdenCompra existing = buscarPorId(id);
        existing.setNumero(datos.getNumero());
        existing.setEstado(datos.getEstado());
        return repo.save(existing);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        repo.deleteById(id);
    }
}
