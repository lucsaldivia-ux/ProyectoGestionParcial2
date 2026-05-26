package com.gestion.almacenes.service.impl;

import com.gestion.almacenes.dto.AlmacenDTO;
import com.gestion.almacenes.model.Almacen;
import com.gestion.almacenes.repository.AlmacenRepository;
import com.gestion.almacenes.service.AlmacenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class AlmacenServiceImpl implements AlmacenService {

    private final AlmacenRepository almacenRepository;

    public AlmacenServiceImpl(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    @Override
    public List<Almacen> listar() {
        log.info("Listando todos los almacenes");
        return almacenRepository.findAll();
    }

    @Override
    public Almacen buscarPorId(Long id) {
        log.info("Buscando almacen con id: {}", id);
        return almacenRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Almacen con id {} no encontrado", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Almacen no encontrado");
                });
    }

    @Override
    public Almacen crear(AlmacenDTO dto) {
        Almacen almacen = new Almacen();
        almacen.setNombre(dto.getNombre());
        almacen.setUbicacion(dto.getUbicacion());
        almacen.setCapacidad(dto.getCapacidad());
        log.info("Creando almacen: {}", dto.getNombre());
        return almacenRepository.save(almacen);
    }

    @Override
    public Almacen actualizar(Long id, AlmacenDTO dto) {
        Almacen almacen = buscarPorId(id);
        almacen.setNombre(dto.getNombre());
        almacen.setUbicacion(dto.getUbicacion());
        almacen.setCapacidad(dto.getCapacidad());
        log.info("Actualizando almacen con id: {}", id);
        return almacenRepository.save(almacen);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        almacenRepository.deleteById(id);
        log.warn("Almacen con id {} eliminado", id);
    }
}
