package com.gestion.cargos.Service;

import com.gestion.cargos.DTO.CargoDTO;
import com.gestion.cargos.DTO.DepartamentoDTO;
import com.gestion.cargos.Model.Cargo;
import com.gestion.cargos.Repository.CargoRepository;
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
public class CargoService {

    @Autowired
    private CargoRepository repo;

    @Autowired
    @Qualifier("webClientDepartamentos")
    private WebClient webClientDepartamentos;

    public List<Cargo> obtenerTodos() {
        log.info("Obteniendo todos los cargos");
        List<Cargo> cargos = repo.findAll();
        log.info("Se encontraron {} cargos", cargos.size());
        return cargos;
    }

    public Optional<Cargo> obtenerPorId(Integer id) {
        log.info("Buscando cargo con id={}", id);
        Optional<Cargo> cargo = repo.findById(id);
        if (cargo.isPresent()) {
            log.info("Cargo encontrado: id={}", id);
        } else {
            log.warn("Cargo no encontrado: id={}", id);
        }
        return cargo;
    }

    public Boolean crearCargo(CargoDTO dto) {
        log.info("Creando cargo: nombre={}", dto.getNombre());

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

        // 2. Mapeo y Guardado
        Cargo c = new Cargo();
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
        c.setDepartamentoId(dto.getDepartamentoId());
        c.setEstado("ACTIVO");
        repo.save(c);
        log.info("Cargo creado exitosamente: nombre={}", dto.getNombre());
        return true;
    }

    public Optional<Cargo> actualizar(Integer id, CargoDTO dto) {
        log.info("Actualizando cargo id={}", id);
        Optional<Cargo> resultado = repo.findById(id).map(c -> {
            c.setNombre(dto.getNombre());
            c.setDescripcion(dto.getDescripcion());
            c.setDepartamentoId(dto.getDepartamentoId());
            return repo.save(c);
        });
        if (resultado.isPresent()) {
            log.info("Cargo id={} actualizado correctamente", id);
        } else {
            log.warn("No se pudo actualizar: cargo id={} no encontrado", id);
        }
        return resultado;
    }

    public boolean eliminar(Integer id) {
        log.info("Desactivando cargo id={}", id);
        boolean resultado = repo.findById(id).map(c -> {
            c.setEstado("INACTIVO");
            repo.save(c);
            return true;
        }).orElse(false);
        if (resultado) {
            log.info("Cargo id={} marcado como INACTIVO", id);
        } else {
            log.warn("No se pudo desactivar: cargo id={} no encontrado", id);
        }
        return resultado;
    }
}
