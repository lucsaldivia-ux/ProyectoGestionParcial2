package com.gestion.auditoria.Controller;

import com.gestion.auditoria.DTO.AuditoriaDTO;
import com.gestion.auditoria.Model.Auditoria;
import com.gestion.auditoria.Service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService service;

    @GetMapping
    public List<Auditoria> listar() {
        return service.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tabla/{tabla}")
    public List<Auditoria> porTabla(@PathVariable String tabla) {
        return service.obtenerPorTabla(tabla);
    }

    @GetMapping("/usuario/{usuario}")
    public List<Auditoria> porUsuario(@PathVariable String usuario) {
        return service.obtenerPorUsuario(usuario);
    }

    @PostMapping("/registrar")
    public Auditoria registrar(@RequestBody AuditoriaDTO dto) {
        return service.registrar(dto);
    }
}
