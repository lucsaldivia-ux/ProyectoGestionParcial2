package com.gestion.existencias.service;

import com.gestion.existencias.dto.ExistenciaDTO;
import com.gestion.existencias.model.Existencia;
import java.util.List;

public interface ExistenciaService {
    List<Existencia> listar();
    Existencia buscarPorId(Long id);
    Existencia crear(ExistenciaDTO dto);
    Existencia actualizar(Long id, ExistenciaDTO dto);
    void eliminar(Long id);
}
