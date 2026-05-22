package com.Operaciones.Notificaciones.Controller;

import com.Operaciones.Notificaciones.Model.Notificacion;
import com.Operaciones.Notificaciones.Service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public List<Notificacion> listar() {
        return notificacionService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@PathVariable Long id) {
        return notificacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Notificacion crear(@RequestBody Notificacion notificacion) {
        return notificacionService.guardar(notificacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(@PathVariable Long id, @RequestBody Notificacion notificacion) {
        return notificacionService.buscarPorId(id).map(n -> {
            notificacion.setId(id);
            return ResponseEntity.ok(notificacionService.guardar(notificacion));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}