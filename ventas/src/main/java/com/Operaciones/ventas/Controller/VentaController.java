package com.Operaciones.ventas.Controller;

import com.Operaciones.ventas.Model.Venta;
import com.Operaciones.ventas.Service.VentaService;
import com.Operaciones.ventas.dto.VentaDTO;
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
@RequestMapping("/api/ventas")
public class VentaController {

    private static final Logger log = LoggerFactory.getLogger(VentaController.class);

    @Autowired
    private VentaService ventaService;

    @GetMapping
    public CollectionModel<EntityModel<Venta>> listar() {
        log.info("Iniciando listado de ventas");
        List<EntityModel<Venta>> ventas = ventaService.listarTodos().stream()
                .map(v -> EntityModel.of(v,
                        linkTo(methodOn(VentaController.class).buscarPorId(v.getId())).withSelfRel(),
                        linkTo(methodOn(VentaController.class).listar()).withRel("ventas")))
                .collect(Collectors.toList());
        log.info("Finalizado listado de ventas - {} registros encontrados", ventas.size());
        return CollectionModel.of(ventas,
                linkTo(methodOn(VentaController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venta>> buscarPorId(@PathVariable Long id) {
        log.info("Iniciando busqueda de venta por id: {}", id);
        return ventaService.buscarPorId(id)
                .map(v -> {
                    log.info("Venta encontrada: {}", v.getId());
                    return ResponseEntity.ok(EntityModel.of(v,
                            linkTo(methodOn(VentaController.class).buscarPorId(v.getId())).withSelfRel(),
                            linkTo(methodOn(VentaController.class).listar()).withRel("ventas")));
                })
                .orElseGet(() -> {
                    log.warn("Venta no encontrada con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Venta> crear(@Valid @RequestBody VentaDTO dto) {
        log.info("Iniciando creacion de venta");
        Venta venta = new Venta();
        venta.setClienteId(dto.getClienteId());
        venta.setProducto(dto.getProducto());
        venta.setCantidad(dto.getCantidad());
        venta.setPrecioUnitario(dto.getPrecioUnitario());
        Venta guardado = ventaService.guardar(venta);
        log.info("Venta creada exitosamente con id: {}", guardado.getId());
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizar(@PathVariable Long id, @Valid @RequestBody VentaDTO dto) {
        log.info("Iniciando actualizacion de venta con id: {}", id);
        ResponseEntity<Venta> resultado = ventaService.actualizar(id, dto)
                .map(v -> {
                    log.info("Venta actualizada exitosamente con id: {}", v.getId());
                    return ResponseEntity.ok(v);
                })
                .orElseGet(() -> {
                    log.warn("Venta no encontrada para actualizar con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Finalizada actualizacion de venta con id: {}", id);
        return resultado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Iniciando eliminacion de venta con id: {}", id);
        ventaService.eliminar(id);
        log.info("Venta eliminada exitosamente con id: {}", id);
        return ResponseEntity.noContent().build();
    }
}