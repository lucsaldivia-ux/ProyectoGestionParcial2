package com.gestion.departamentos.Service;

import com.gestion.departamentos.DTO.DepartamentoDTO;
import com.gestion.departamentos.Model.Departamento;
import com.gestion.departamentos.Repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repo;

    public List<Departamento> obtenerTodos() {
        log.info("Obteniendo todos los departamentos");
        List<Departamento> departamentos = repo.findAll();
        log.info("Se encontraron {} departamentos", departamentos.size());
        return departamentos;
    }

    public Optional<Departamento> obtenerPorId(Integer id) {
        log.info("Buscando departamento con id={}", id);
        Optional<Departamento> departamento = repo.findById(id);
        if (departamento.isPresent()) {
            log.info("Departamento encontrado: id={}", id);
        } else {
            log.warn("Departamento no encontrado: id={}", id);
        }
        return departamento;
    }

    public Departamento crear(DepartamentoDTO dto) {
        log.info("Creando departamento: nombre={}", dto.getNombre());
        Departamento d = new Departamento();
        d.setNombre(dto.getNombre());
        d.setDescripcion(dto.getDescripcion());
        d.setEstado("ACTIVO");
        Departamento guardado = repo.save(d);
        log.info("Departamento creado exitosamente con id={}", guardado.getId());
        return guardado;
    }

    public Optional<Departamento> actualizar(Integer id, DepartamentoDTO dto) {
        log.info("Actualizando departamento id={}", id);
        Optional<Departamento> resultado = repo.findById(id).map(d -> {
            d.setNombre(dto.getNombre());
            d.setDescripcion(dto.getDescripcion());
            return repo.save(d);
        });
        if (resultado.isPresent()) {
            log.info("Departamento id={} actualizado correctamente", id);
        } else {
            log.warn("No se pudo actualizar: departamento id={} no encontrado", id);
        }
        return resultado;
    }

    public boolean eliminar(Integer id) {
        log.info("Desactivando departamento id={}", id);
        boolean resultado = repo.findById(id).map(d -> {
            d.setEstado("INACTIVO");
            repo.save(d);
            return true;
        }).orElse(false);
        if (resultado) {
            log.info("Departamento id={} marcado como INACTIVO", id);
        } else {
            log.warn("No se pudo desactivar: departamento id={} no encontrado", id);
        }
        return resultado;
    }
}
