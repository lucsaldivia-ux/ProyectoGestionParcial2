package com.gestion.gestionProject.inventario.productos;

import java.util.List;

public interface ProductoService {
    List<Producto> listar();
    Producto buscarPorId(Long id);
    Producto crear(Producto producto);
    Producto actualizar(Long id, Producto producto);
    void eliminar(Long id);
}
