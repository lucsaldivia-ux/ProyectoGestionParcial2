package com.grupo.gestion.empleados.service;
import com.grupo.gestion.empleados.dto.EmpleadoDTO;
import com.grupo.gestion.empleados.model.Empleado;
import com.grupo.gestion.empleados.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class EmpleadoService {
    @Autowired private EmpleadoRepository repo;
    public List<Empleado> obtenerTodos() { return repo.findAll(); }
    public Optional<Empleado> obtenerPorId(Integer id) { return repo.findById(id); }
    public Empleado crear(EmpleadoDTO dto) {
        Empleado e = new Empleado();
        e.setNombre(dto.getNombre()); e.setApellido(dto.getApellido());
        e.setEmail(dto.getEmail()); e.setTelefono(dto.getTelefono());
        e.setEstado("ACTIVO");
        return repo.save(e);
    }
    public Optional<Empleado> actualizar(Integer id, EmpleadoDTO dto) {
        return repo.findById(id).map(e -> {
            e.setNombre(dto.getNombre()); e.setApellido(dto.getApellido());
            e.setEmail(dto.getEmail()); e.setTelefono(dto.getTelefono());
            return repo.save(e);
        });
    }
    public boolean eliminar(Integer id) {
        return repo.findById(id).map(e -> { e.setEstado("INACTIVO"); repo.save(e); return true; }).orElse(false);
    }
}
