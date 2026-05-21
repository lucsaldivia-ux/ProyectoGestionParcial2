package com.gestion.empleados.Service;

import com.gestion.empleados.DTO.CargoDTO;
import com.gestion.empleados.DTO.DepartamentoDTO;
import com.gestion.empleados.DTO.EmpleadoDTO;
import com.gestion.empleados.Model.Empleado;
import com.gestion.empleados.Repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    @Autowired
    @Qualifier("webClientDepartamentos")
    private WebClient webClientDepartamentos;

    @Autowired
    @Qualifier("webClientCargos")
    private WebClient webClientCargos;

    public List<Empleado> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Empleado> obtenerPorId(Integer id) {
        return repo.findById(id);
    }

    public Boolean crearEmpleado(EmpleadoDTO dto) {
        // 1. Buscamos el Departamento (Bloqueante)
        DepartamentoDTO depto = webClientDepartamentos.get()
                .uri("/departamentos/{id}", dto.getDepartamentoId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Departamento no encontrado")))
                .bodyToMono(DepartamentoDTO.class)
                .block();

        if (depto == null) return false;

        // 2. Buscamos el Cargo (Bloqueante)
        CargoDTO cargo = webClientCargos.get()
                .uri("/cargos/{id}", dto.getCargoId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Cargo no encontrado")))
                .bodyToMono(CargoDTO.class)
                .block();

        if (cargo == null) return false;

        // 3. Mapeo y Guardado
        Empleado emp = new Empleado();
        emp.setNombre(dto.getNombre());
        emp.setApellido(dto.getApellido());
        emp.setEmail(dto.getEmail());
        emp.setTelefono(dto.getTelefono());
        emp.setDepartamentoId(dto.getDepartamentoId());
        emp.setCargoId(dto.getCargoId());
        emp.setEstado("ACTIVO");
        repo.save(emp);
        return true;
    }

    public Optional<Empleado> actualizar(Integer id, EmpleadoDTO dto) {
        return repo.findById(id).map(e -> {
            e.setNombre(dto.getNombre());
            e.setApellido(dto.getApellido());
            e.setEmail(dto.getEmail());
            e.setTelefono(dto.getTelefono());
            e.setDepartamentoId(dto.getDepartamentoId());
            e.setCargoId(dto.getCargoId());
            return repo.save(e);
        });
    }

    public boolean eliminar(Integer id) {
        return repo.findById(id).map(e -> {
            e.setEstado("INACTIVO");
            repo.save(e);
            return true;
        }).orElse(false);
    }
}
