package com.gestion.proveedores.service;

import com.gestion.proveedores.dto.ProveedorDTO;
import com.gestion.proveedores.model.Proveedor;
import java.util.List;

public interface ProveedorService {
    List<Proveedor> listar();
    Proveedor buscarPorId(Long id);
    Proveedor crear(ProveedorDTO dto);
    Proveedor actualizar(Long id, ProveedorDTO dto);
    void eliminar(Long id);
}
