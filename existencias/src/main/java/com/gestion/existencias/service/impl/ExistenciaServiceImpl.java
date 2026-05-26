package com.gestion.existencias.service.impl;

import com.gestion.existencias.dto.ExistenciaDTO;
import com.gestion.existencias.model.Existencia;
import com.gestion.existencias.repository.ExistenciaRepository;
import com.gestion.existencias.service.ExistenciaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ExistenciaServiceImpl implements ExistenciaService {

    private final ExistenciaRepository existenciaRepository;

    public ExistenciaServiceImpl(ExistenciaRepository existenciaRepository) {
        this.existenciaRepository = existenciaRepository;
    }

    @Override
    public List<Existencia> listar() {
        log.info("Listando todas las existencias");
        return existenciaRepository.findAll();
    }

    @Override
    public Existencia buscarPorId(Long id) {
        log.info("Buscando existencia con id: {}", id);
        return existenciaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Existencia con id {} no encontrada", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Existencia no encontrada");
                });
    }

    @Override
    public Existencia crear(ExistenciaDTO dto) {
        Existencia existencia = new Existencia();
        existencia.setProductoId(dto.getProductoId());
        existencia.setCantidad(dto.getCantidad());
        existencia.setUbicacion(dto.getUbicacion());
        log.info("Creando existencia para producto id: {}", dto.getProductoId());
        return existenciaRepository.save(existencia);
    }

    @Override
    public Existencia actualizar(Long id, ExistenciaDTO dto) {
        Existencia existencia = buscarPorId(id);
        existencia.setProductoId(dto.getProductoId());
        existencia.setCantidad(dto.getCantidad());
        existencia.setUbicacion(dto.getUbicacion());
        log.info("Actualizando existencia con id: {}", id);
        return existenciaRepository.save(existencia);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        existenciaRepository.deleteById(id);
        log.warn("Existencia con id {} eliminada", id);
    }
}
