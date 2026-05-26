package com.gestion.ordenescompra.service;

import com.gestion.ordenescompra.dto.OrdenCompraDTO;
import com.gestion.ordenescompra.model.OrdenCompra;
import java.util.List;

public interface OrdenCompraService {
    List<OrdenCompra> listar();
    OrdenCompra buscarPorId(Long id);
    OrdenCompra crear(OrdenCompraDTO dto);
    OrdenCompra actualizar(Long id, OrdenCompraDTO dto);
    void eliminar(Long id);
}
