package com.Operaciones.Notificaciones.Controller;

import com.Operaciones.Notificaciones.Model.Notificacion;
import com.Operaciones.Notificaciones.Service.NotificacionService;
import com.Operaciones.Notificaciones.dto.NotificacionDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private static final Logger log = LoggerFactory.getLogger(NotificacionController.class);

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public CollectionModel<EntityModel<Notificacion>> listar() {
        log.info("Iniciando listado de notificaciones");
        List<EntityModel<Notificacion>> notificaciones = notificacionService.listarTodos().stream()
                .map(n -> EntityModel.of(n,
                        linkTo(methodOn(NotificacionController.class).buscarPorId(n.getId())).withSelfRel(),
                        linkTo(methodOn(NotificacionController.class).listar()).withRel("notificaciones")))
                .collect(Collectors.toList());
        log.info("Finalizado listado de notificaciones - {} registros encontrados", notificaciones.size());
        return CollectionModel.of(notificaciones,
                linkTo(methodOn(NotificacionController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Notificacion>> buscarPorId(@PathVariable Long id) {
        log.info("Iniciando busqueda de notificacion por id: {}", id);
        return notificacionService.buscarPorId(id)
                .map(n -> {
                    log.info("Notificacion encontrada: {}", n.getId());
                    return ResponseEntity.ok(EntityModel.of(n,
                            linkTo(methodOn(NotificacionController.class).buscarPorId(n.getId())).withSelfRel(),
                            linkTo(methodOn(NotificacionController.class).listar()).withRel("notificaciones")));
                })
                .orElseGet(() -> {
                    log.warn("Notificacion no encontrada con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody NotificacionDTO dto) {
        log.info("Iniciando creacion de notificacion");
        Notificacion notificacion = new Notificacion();
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setDestinatario(dto.getDestinatario());
        notificacion.setEstado(dto.getEstado());
        Notificacion guardado = notificacionService.guardar(notificacion);
        log.info("Notificacion creada exitosamente con id: {}", guardado.getId());
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(@PathVariable Long id, @Valid @RequestBody NotificacionDTO dto) {
        log.info("Iniciando actualizacion de notificacion con id: {}", id);
        ResponseEntity<Notificacion> resultado = notificacionService.actualizar(id, dto)
                .map(n -> {
                    log.info("Notificacion actualizada exitosamente con id: {}", n.getId());
                    return ResponseEntity.ok(n);
                })
                .orElseGet(() -> {
                    log.warn("Notificacion no encontrada para actualizar con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Finalizada actualizacion de notificacion con id: {}", id);
        return resultado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Iniciando eliminacion de notificacion con id: {}", id);
        notificacionService.eliminar(id);
        log.info("Notificacion eliminada exitosamente con id: {}", id);
        return ResponseEntity.noContent().build();
    }
}