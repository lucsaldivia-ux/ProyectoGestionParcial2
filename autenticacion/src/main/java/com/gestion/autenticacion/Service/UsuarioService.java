package com.gestion.autenticacion.Service;

import com.gestion.autenticacion.DTO.EmpleadoDTO;
import com.gestion.autenticacion.DTO.UsuarioDTO;
import com.gestion.autenticacion.Model.Usuario;
import com.gestion.autenticacion.Repository.UsuarioRepository;
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
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    @Qualifier("webClientEmpleados")
    private WebClient webClientEmpleados;

    public List<Usuario> obtenerTodos() {
        return repo.findAll();
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return repo.findById(id);
    }

    public Boolean registrar(UsuarioDTO dto) {
        // 1. Buscamos el Empleado (Bloqueante)
        EmpleadoDTO emp = webClientEmpleados.get()
                .uri("/empleados/{id}", dto.getEmpleadoId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RuntimeException("Empleado no encontrado")))
                .bodyToMono(EmpleadoDTO.class)
                .block();

        if (emp == null) return false;

        // 2. Mapeo y Guardado
        Usuario u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setPassword(dto.getPassword());
        u.setRol(dto.getRol() != null ? dto.getRol() : "USER");
        u.setEmpleadoId(dto.getEmpleadoId());
        u.setEstado("ACTIVO");
        repo.save(u);
        return true;
    }

    public Optional<Usuario> login(String username, String password) {
        return repo.findByUsernameAndPassword(username, password);
    }

    public boolean eliminar(Integer id) {
        return repo.findById(id).map(u -> {
            u.setEstado("INACTIVO");
            repo.save(u);
            return true;
        }).orElse(false);
    }
}
