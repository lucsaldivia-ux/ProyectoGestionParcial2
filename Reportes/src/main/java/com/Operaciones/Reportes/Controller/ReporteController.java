package com.Operaciones.Reportes.Controller;

import com.Operaciones.Reportes.Model.Reporte;
import com.Operaciones.Reportes.Service.ReporteService;
import com.Operaciones.Reportes.dto.ReporteDTO;
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
@RequestMapping("/api/reportes")
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public CollectionModel<EntityModel<Reporte>> listar() {
        log.info("Iniciando listado de reportes");
        List<EntityModel<Reporte>> reportes = reporteService.listarTodos().stream()
                .map(r -> EntityModel.of(r,
                        linkTo(methodOn(ReporteController.class).buscarPorId(r.getId())).withSelfRel(),
                        linkTo(methodOn(ReporteController.class).listar()).withRel("reportes")))
                .collect(Collectors.toList());
        log.info("Finalizado listado de reportes - {} registros encontrados", reportes.size());
        return CollectionModel.of(reportes,
                linkTo(methodOn(ReporteController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reporte>> buscarPorId(@PathVariable Long id) {
        log.info("Iniciando busqueda de reporte por id: {}", id);
        return reporteService.buscarPorId(id)
                .map(r -> {
                    log.info("Reporte encontrado: {}", r.getId());
                    return ResponseEntity.ok(EntityModel.of(r,
                            linkTo(methodOn(ReporteController.class).buscarPorId(r.getId())).withSelfRel(),
                            linkTo(methodOn(ReporteController.class).listar()).withRel("reportes")));
                })
                .orElseGet(() -> {
                    log.warn("Reporte no encontrado con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Reporte> crear(@Valid @RequestBody ReporteDTO dto) {
        log.info("Iniciando creacion de reporte");
        Reporte reporte = new Reporte();
        reporte.setTipo(dto.getTipo());
        reporte.setDescripcion(dto.getDescripcion());
        Reporte guardado = reporteService.guardar(reporte);
        log.info("Reporte creado exitosamente con id: {}", guardado.getId());
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reporte> actualizar(@PathVariable Long id, @Valid @RequestBody ReporteDTO dto) {
        log.info("Iniciando actualizacion de reporte con id: {}", id);
        ResponseEntity<Reporte> resultado = reporteService.actualizar(id, dto)
                .map(r -> {
                    log.info("Reporte actualizado exitosamente con id: {}", r.getId());
                    return ResponseEntity.ok(r);
                })
                .orElseGet(() -> {
                    log.warn("Reporte no encontrado para actualizar con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Finalizada actualizacion de reporte con id: {}", id);
        return resultado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Iniciando eliminacion de reporte con id: {}", id);
        reporteService.eliminar(id);
        log.info("Reporte eliminado exitosamente con id: {}", id);
        return ResponseEntity.noContent().build();
    }
}