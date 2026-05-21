package com.gestion.departamentos.Service;

import com.gestion.departamentos.DTO.DepartamentoDTO;
import com.gestion.departamentos.Model.Departamento;
import com.gestion.departamentos.Repository.DepartamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repo;

    public List<Departamento> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Departamento> obtenerPorId(Integer id) {
        return repo.findById(id);
    }

    public Departamento crear(DepartamentoDTO dto) {
        Departamento d = new Departamento();
        d.setNombre(dto.getNombre());
        d.setDescripcion(dto.getDescripcion());
        d.setEstado("ACTIVO");
        return repo.save(d);
    }

    public Optional<Departamento> actualizar(Integer id, DepartamentoDTO dto) {
        return repo.findById(id).map(d -> {
            d.setNombre(dto.getNombre());
            d.setDescripcion(dto.getDescripcion());
            return repo.save(d);
        });
    }

    public boolean eliminar(Integer id) {
        return repo.findById(id).map(d -> {
            d.setEstado("INACTIVO");
            repo.save(d);
            return true;
        }).orElse(false);
    }
}
