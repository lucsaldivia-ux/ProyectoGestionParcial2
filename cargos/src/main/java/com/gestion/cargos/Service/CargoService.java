package com.gestion.cargos.Service;

import com.gestion.cargos.DTO.CargoDTO;
import com.gestion.cargos.DTO.DepartamentoDTO;
import com.gestion.cargos.Model.Cargo;
import com.gestion.cargos.Repository.CargoRepository;
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
public class CargoService {

    @Autowired
    private CargoRepository repo;

    @Autowired
    @Qualifier("webClientDepartamentos")
    private WebClient webClientDepartamentos;

    public List<Cargo> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Cargo> obtenerPorId(Integer id) {
        return repo.findById(id);
    }

    public Boolean crearCargo(CargoDTO dto) {
        // 1. Buscamos el Departamento (Bloqueante)
        DepartamentoDTO depto = webClientDepartamentos.get()
                .uri("/departamentos/{id}", dto.getDepartamentoId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Departamento no encontrado")))
                .bodyToMono(DepartamentoDTO.class)
                .block();

        if (depto == null) return false;

        // 2. Mapeo y Guardado
        Cargo c = new Cargo();
        c.setNombre(dto.getNombre());
        c.setDescripcion(dto.getDescripcion());
        c.setDepartamentoId(dto.getDepartamentoId());
        c.setEstado("ACTIVO");
        repo.save(c);
        return true;
    }

    public Optional<Cargo> actualizar(Integer id, CargoDTO dto) {
        return repo.findById(id).map(c -> {
            c.setNombre(dto.getNombre());
            c.setDescripcion(dto.getDescripcion());
            c.setDepartamentoId(dto.getDepartamentoId());
            return repo.save(c);
        });
    }

    public boolean eliminar(Integer id) {
        return repo.findById(id).map(c -> {
            c.setEstado("INACTIVO");
            repo.save(c);
            return true;
        }).orElse(false);
    }
}
