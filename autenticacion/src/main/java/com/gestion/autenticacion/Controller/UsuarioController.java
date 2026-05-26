package com.gestion.autenticacion.Controller;

import com.gestion.autenticacion.DTO.UsuarioDTO;
import com.gestion.autenticacion.Model.Usuario;
import com.gestion.autenticacion.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/autenticacion")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listar() {
        log.info("GET /api/v1/autenticacion/usuarios - Listando todos los usuarios");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Integer id) {
        log.info("GET /api/v1/autenticacion/usuarios/{} - Buscando usuario", id);
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@Valid @RequestBody UsuarioDTO dto) {
        log.info("POST /api/v1/autenticacion/registrar - Registrando usuario: {}", dto.getUsername());
        Boolean save = service.registrar(dto);
        if (!save) {
            log.warn("No se pudo registrar el usuario: empleado no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Empleado no existe en su microservicio.");
        }
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO dto) {
        log.info("POST /api/v1/autenticacion/login - Intento de login: {}", dto.getUsername());
        return service.login(dto.getUsername(), dto.getPassword())
                .map(u -> ResponseEntity.ok("Login exitoso. Rol: " + u.getRol()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas"));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("DELETE /api/v1/autenticacion/usuarios/{} - Desactivando usuario", id);
        if (service.eliminar(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
