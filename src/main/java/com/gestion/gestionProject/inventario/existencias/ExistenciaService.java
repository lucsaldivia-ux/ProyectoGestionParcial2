package com.gestion.gestionProject.inventario.existencias;

import java.util.List;

public interface ExistenciaService {
    List<Existencia> listar();
    Existencia buscarPorId(Long id);
    Existencia crear(Existencia existencia);
    Existencia actualizar(Long id, Existencia existencia);
    void eliminar(Long id);
}
