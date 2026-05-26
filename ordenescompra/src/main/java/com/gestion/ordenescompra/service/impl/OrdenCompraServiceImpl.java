package com.gestion.ordenescompra.service.impl;

import com.gestion.ordenescompra.dto.OrdenCompraDTO;
import com.gestion.ordenescompra.model.OrdenCompra;
import com.gestion.ordenescompra.repository.OrdenCompraRepository;
import com.gestion.ordenescompra.service.OrdenCompraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;

    public OrdenCompraServiceImpl(OrdenCompraRepository ordenCompraRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
    }

    @Override
    public List<OrdenCompra> listar() {
        log.info("Listando todas las ordenes de compra");
        return ordenCompraRepository.findAll();
    }

    @Override
    public OrdenCompra buscarPorId(Long id) {
        log.info("Buscando orden de compra con id: {}", id);
        return ordenCompraRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Orden de compra con id {} no encontrada", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden de compra no encontrada");
                });
    }

    @Override
    public OrdenCompra crear(OrdenCompraDTO dto) {
        OrdenCompra orden = new OrdenCompra();
        orden.setProveedorId(dto.getProveedorId());
        orden.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        orden.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");
        orden.setTotal(dto.getTotal());
        log.info("Creando orden de compra para proveedor id: {}", dto.getProveedorId());
        return ordenCompraRepository.save(orden);
    }

    @Override
    public OrdenCompra actualizar(Long id, OrdenCompraDTO dto) {
        OrdenCompra orden = buscarPorId(id);
        orden.setProveedorId(dto.getProveedorId());
        orden.setFecha(dto.getFecha());
        orden.setEstado(dto.getEstado());
        orden.setTotal(dto.getTotal());
        log.info("Actualizando orden de compra con id: {}", id);
        return ordenCompraRepository.save(orden);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        ordenCompraRepository.deleteById(id);
        log.warn("Orden de compra con id {} eliminada", id);
    }
}
