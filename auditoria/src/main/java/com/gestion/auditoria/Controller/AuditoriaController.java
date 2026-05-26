package com.gestion.auditoria.Controller;

import com.gestion.auditoria.DTO.AuditoriaDTO;
import com.gestion.auditoria.Model.Auditoria;
import com.gestion.auditoria.Service.AuditoriaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService service;

    @GetMapping
    public ResponseEntity<List<Auditoria>> listar() {
        log.info("GET /api/v1/auditoria - Listando todos los registros de auditoría");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> obtener(@PathVariable Integer id) {
        log.info("GET /api/v1/auditoria/{} - Buscando registro de auditoría", id);
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tabla/{tabla}")
    public ResponseEntity<List<Auditoria>> porTabla(@PathVariable String tabla) {
        log.info("GET /api/v1/auditoria/tabla/{} - Buscando por tabla", tabla);
        return ResponseEntity.ok(service.obtenerPorTabla(tabla));
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<List<Auditoria>> porUsuario(@PathVariable String usuario) {
        log.info("GET /api/v1/auditoria/usuario/{} - Buscando por usuario", usuario);
        return ResponseEntity.ok(service.obtenerPorUsuario(usuario));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Auditoria> registrar(@Valid @RequestBody AuditoriaDTO dto) {
        log.info("POST /api/v1/auditoria/registrar - Registrando auditoría: accion={}", dto.getAccion());
        return ResponseEntity.ok(service.registrar(dto));
    }
}
