package com.gestion.empleados.Service;

import com.gestion.empleados.DTO.CargoDTO;
import com.gestion.empleados.DTO.DepartamentoDTO;
import com.gestion.empleados.DTO.EmpleadoDTO;
import com.gestion.empleados.Model.Empleado;
import com.gestion.empleados.Repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("Obteniendo todos los empleados");
        List<Empleado> empleados = repo.findAll();
        log.info("Se encontraron {} empleados", empleados.size());
        return empleados;
    }

    public Optional<Empleado> obtenerPorId(Integer id) {
        log.info("Buscando empleado con id={}", id);
        Optional<Empleado> empleado = repo.findById(id);
        if (empleado.isPresent()) {
            log.info("Empleado encontrado: id={}", id);
        } else {
            log.warn("Empleado no encontrado: id={}", id);
        }
        return empleado;
    }

    public Boolean crearEmpleado(EmpleadoDTO dto) {
        log.info("Creando empleado: nombre={} apellido={}", dto.getNombre(), dto.getApellido());

        // 1. Buscamos el Departamento (Bloqueante)
        DepartamentoDTO depto;
        try {
            depto = webClientDepartamentos.get()
                    .uri("/departamentos/{id}", dto.getDepartamentoId())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response ->
                            Mono.error(new RuntimeException("Departamento no encontrado")))
                    .bodyToMono(DepartamentoDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al consultar departamento id={}: {}", dto.getDepartamentoId(), e.getMessage());
            return false;
        }

        if (depto == null) {
            log.warn("Departamento id={} no existe", dto.getDepartamentoId());
            return false;
        }

        // 2. Buscamos el Cargo (Bloqueante)
        CargoDTO cargo;
        try {
            cargo = webClientCargos.get()
                    .uri("/cargos/{id}", dto.getCargoId())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response ->
                            Mono.error(new RuntimeException("Cargo no encontrado")))
                    .bodyToMono(CargoDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al consultar cargo id={}: {}", dto.getCargoId(), e.getMessage());
            return false;
        }

        if (cargo == null) {
            log.warn("Cargo id={} no existe", dto.getCargoId());
            return false;
        }

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
        log.info("Empleado creado exitosamente: nombre={} apellido={}", dto.getNombre(), dto.getApellido());
        return true;
    }

    public Optional<Empleado> actualizar(Integer id, EmpleadoDTO dto) {
        log.info("Actualizando empleado id={}", id);
        Optional<Empleado> resultado = repo.findById(id).map(e -> {
            e.setNombre(dto.getNombre());
            e.setApellido(dto.getApellido());
            e.setEmail(dto.getEmail());
            e.setTelefono(dto.getTelefono());
            e.setDepartamentoId(dto.getDepartamentoId());
            e.setCargoId(dto.getCargoId());
            return repo.save(e);
        });
        if (resultado.isPresent()) {
            log.info("Empleado id={} actualizado correctamente", id);
        } else {
            log.warn("No se pudo actualizar: empleado id={} no encontrado", id);
        }
        return resultado;
    }

    public boolean eliminar(Integer id) {
        log.info("Desactivando empleado id={}", id);
        boolean resultado = repo.findById(id).map(e -> {
            e.setEstado("INACTIVO");
            repo.save(e);
            return true;
        }).orElse(false);
        if (resultado) {
            log.info("Empleado id={} marcado como INACTIVO", id);
        } else {
            log.warn("No se pudo desactivar: empleado id={} no encontrado", id);
        }
        return resultado;
    }
}
