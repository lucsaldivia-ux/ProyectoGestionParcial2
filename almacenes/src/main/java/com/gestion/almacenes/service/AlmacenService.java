package com.gestion.almacenes.service;

import com.gestion.almacenes.dto.AlmacenDTO;
import com.gestion.almacenes.model.Almacen;
import java.util.List;

public interface AlmacenService {
    List<Almacen> listar();
    Almacen buscarPorId(Long id);
    Almacen crear(AlmacenDTO dto);
    Almacen actualizar(Long id, AlmacenDTO dto);
    void eliminar(Long id);
}
