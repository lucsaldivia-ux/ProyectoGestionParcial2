package com.gestion.gestionProject.inventario.almacenes;

import java.util.List;

public interface AlmacenService {
    List<Almacen> listar();
    Almacen buscarPorId(Long id);
    Almacen crear(Almacen almacen);
    Almacen actualizar(Long id, Almacen almacen);
    void eliminar(Long id);
}
