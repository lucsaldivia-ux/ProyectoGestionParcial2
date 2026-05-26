package com.gestion.proveedores.service.impl;

import com.gestion.proveedores.dto.ProveedorDTO;
import com.gestion.proveedores.model.Proveedor;
import com.gestion.proveedores.repository.ProveedorRepository;
import com.gestion.proveedores.service.ProveedorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public List<Proveedor> listar() {
        log.info("Listando todos los proveedores");
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor buscarPorId(Long id) {
        log.info("Buscando proveedor con id: {}", id);
        return proveedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Proveedor con id {} no encontrado", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
                });
    }

    @Override
    public Proveedor crear(ProveedorDTO dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        log.info("Creando proveedor: {}", dto.getNombre());
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Long id, ProveedorDTO dto) {
        Proveedor proveedor = buscarPorId(id);
        proveedor.setNombre(dto.getNombre());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
        log.info("Actualizando proveedor con id: {}", id);
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        proveedorRepository.deleteById(id);
        log.warn("Proveedor con id {} eliminado", id);
    }
}
