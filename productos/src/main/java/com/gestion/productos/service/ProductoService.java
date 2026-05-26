package com.gestion.productos.service;

import com.gestion.productos.dto.ProductoDTO;
import com.gestion.productos.model.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> listar();
    Producto buscarPorId(Long id);
    Producto crear(ProductoDTO dto);
    Producto actualizar(Long id, ProductoDTO dto);
    void eliminar(Long id);
}
