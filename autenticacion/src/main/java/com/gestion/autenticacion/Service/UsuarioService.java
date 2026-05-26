package com.gestion.autenticacion.Service;

import com.gestion.autenticacion.DTO.EmpleadoDTO;
import com.gestion.autenticacion.DTO.UsuarioDTO;
import com.gestion.autenticacion.Model.Usuario;
import com.gestion.autenticacion.Repository.UsuarioRepository;
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
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    @Qualifier("webClientEmpleados")
    private WebClient webClientEmpleados;

    public List<Usuario> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        List<Usuario> usuarios = repo.findAll();
        log.info("Se encontraron {} usuarios", usuarios.size());
        return usuarios;
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        log.info("Buscando usuario con id={}", id);
        Optional<Usuario> usuario = repo.findById(id);
        if (usuario.isPresent()) {
            log.info("Usuario encontrado: id={}", id);
        } else {
            log.warn("Usuario no encontrado: id={}", id);
        }
        return usuario;
    }

    public Boolean registrar(UsuarioDTO dto) {
        log.info("Registrando usuario: username={}", dto.getUsername());

        // 1. Buscamos el Empleado (Bloqueante)
        EmpleadoDTO emp;
        try {
            emp = webClientEmpleados.get()
                    .uri("/empleados/{id}", dto.getEmpleadoId())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response ->
                            Mono.error(new RuntimeException("Empleado no encontrado")))
                    .bodyToMono(EmpleadoDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al consultar empleado id={}: {}", dto.getEmpleadoId(), e.getMessage());
            return false;
        }

        if (emp == null) {
            log.warn("Empleado id={} no existe", dto.getEmpleadoId());
            return false;
        }

        // 2. Mapeo y Guardado
        Usuario u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setPassword(dto.getPassword());
        u.setRol(dto.getRol() != null ? dto.getRol() : "USER");
        u.setEmpleadoId(dto.getEmpleadoId());
        u.setEstado("ACTIVO");
        repo.save(u);
        log.info("Usuario registrado exitosamente: username={}", dto.getUsername());
        return true;
    }

    public Optional<Usuario> login(String username, String password) {
        log.info("Intento de login: username={}", username);
        Optional<Usuario> usuario = repo.findByUsernameAndPassword(username, password);
        if (usuario.isPresent()) {
            log.info("Login exitoso: username={}", username);
        } else {
            log.warn("Login fallido: username={}", username);
        }
        return usuario;
    }

    public boolean eliminar(Integer id) {
        log.info("Desactivando usuario id={}", id);
        boolean resultado = repo.findById(id).map(u -> {
            u.setEstado("INACTIVO");
            repo.save(u);
            return true;
        }).orElse(false);
        if (resultado) {
            log.info("Usuario id={} marcado como INACTIVO", id);
        } else {
            log.warn("No se pudo desactivar: usuario id={} no encontrado", id);
        }
        return resultado;
    }
}
