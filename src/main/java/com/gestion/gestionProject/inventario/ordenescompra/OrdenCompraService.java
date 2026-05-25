package com.gestion.gestionProject.inventario.ordenescompra;

import java.util.List;

public interface OrdenCompraService {
    List<OrdenCompra> listar();
    OrdenCompra buscarPorId(Long id);
    OrdenCompra crear(OrdenCompra orden);
    OrdenCompra actualizar(Long id, OrdenCompra orden);
    void eliminar(Long id);
}
