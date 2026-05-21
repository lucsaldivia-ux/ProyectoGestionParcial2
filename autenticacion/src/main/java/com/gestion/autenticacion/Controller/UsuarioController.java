package com.gestion.autenticacion.Controller;

import com.gestion.autenticacion.DTO.UsuarioDTO;
import com.gestion.autenticacion.Model.Usuario;
import com.gestion.autenticacion.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/autenticacion")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/usuarios")
    public List<Usuario> listar() {
        return service.obtenerTodos();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody UsuarioDTO dto) {
        Boolean save = service.registrar(dto);
        if (!save) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Empleado no existe en su microservicio.");
        }
        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioDTO dto) {
        return service.login(dto.getUsername(), dto.getPassword())
                .map(u -> ResponseEntity.ok("Login exitoso. Rol: " + u.getRol()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas"));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (service.eliminar(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
