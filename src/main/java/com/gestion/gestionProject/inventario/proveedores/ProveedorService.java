package com.gestion.gestionProject.inventario.proveedores;

import java.util.List;

public interface ProveedorService {
    List<Proveedor> listar();
    Proveedor buscarPorId(Long id);
    Proveedor crear(Proveedor proveedor);
    Proveedor actualizar(Long id, Proveedor proveedor);
    void eliminar(Long id);
}
