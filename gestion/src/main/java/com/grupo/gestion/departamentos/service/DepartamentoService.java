package com.grupo.gestion.departamentos.service;
import com.grupo.gestion.departamentos.dto.DepartamentoDTO;
import com.grupo.gestion.departamentos.model.Departamento;
import com.grupo.gestion.departamentos.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.Optional;
@Service
public class DepartamentoService {
    @Autowired private DepartamentoRepository repo;
    public List<Departamento> obtenerTodos() { return repo.findAll(); }
    public Optional<Departamento> obtenerPorId(Integer id) { return repo.findById(id); }
    public Departamento crear(DepartamentoDTO dto) {
        Departamento d = new Departamento();
        d.setNombre(dto.getNombre()); d.setDescripcion(dto.getDescripcion()); d.setEstado("ACTIVO");
        return repo.save(d);
    }
    public Optional<Departamento> actualizar(Integer id, DepartamentoDTO dto) {
        return repo.findById(id).map(d -> { d.setNombre(dto.getNombre()); d.setDescripcion(dto.getDescripcion()); return repo.save(d); });
    }
    public boolean eliminar(Integer id) {
        return repo.findById(id).map(d -> { d.setEstado("INACTIVO"); repo.save(d); return true; }).orElse(false);
    }
}
