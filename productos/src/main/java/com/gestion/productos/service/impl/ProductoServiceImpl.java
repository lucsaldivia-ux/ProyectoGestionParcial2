package com.gestion.productos.service.impl;

import com.gestion.productos.dto.ProductoDTO;
import com.gestion.productos.model.Producto;
import com.gestion.productos.repository.ProductoRepository;
import com.gestion.productos.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listar() {
        log.info("Listando todos los productos");
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        log.info("Buscando producto con id: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto con id {} no encontrado", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
                });
    }

    @Override
    public Producto crear(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        log.info("Creando producto: {}", dto.getNombre());
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Long id, ProductoDTO dto) {
        Producto producto = buscarPorId(id);
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        log.info("Actualizando producto con id: {}", id);
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        productoRepository.deleteById(id);
        log.warn("Producto con id {} eliminado", id);
    }
}
