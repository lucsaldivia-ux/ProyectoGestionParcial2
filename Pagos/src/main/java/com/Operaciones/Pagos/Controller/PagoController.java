package com.Operaciones.Pagos.Controller;

import com.Operaciones.Pagos.Model.Pago;
import com.Operaciones.Pagos.Service.PagoService;
import com.Operaciones.Pagos.dto.PagoDTO;
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
@RequestMapping("/api/pagos")
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public CollectionModel<EntityModel<Pago>> listar() {
        log.info("Iniciando listado de pagos");
        List<EntityModel<Pago>> pagos = pagoService.listarTodos().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PagoController.class).buscarPorId(p.getId())).withSelfRel(),
                        linkTo(methodOn(PagoController.class).listar()).withRel("pagos")))
                .collect(Collectors.toList());
        log.info("Finalizado listado de pagos - {} registros encontrados", pagos.size());
        return CollectionModel.of(pagos,
                linkTo(methodOn(PagoController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> buscarPorId(@PathVariable Long id) {
        log.info("Iniciando busqueda de pago por id: {}", id);
        return pagoService.buscarPorId(id)
                .map(p -> {
                    log.info("Pago encontrado: {}", p.getId());
                    return ResponseEntity.ok(EntityModel.of(p,
                            linkTo(methodOn(PagoController.class).buscarPorId(p.getId())).withSelfRel(),
                            linkTo(methodOn(PagoController.class).listar()).withRel("pagos")));
                })
                .orElseGet(() -> {
                    log.warn("Pago no encontrado con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO dto) {
        log.info("Iniciando creacion de pago");
        Pago pago = new Pago();
        pago.setVentaId(dto.getVentaId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(dto.getEstado());
        Pago guardado = pagoService.guardar(pago);
        log.info("Pago creado exitosamente con id: {}", guardado.getId());
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @Valid @RequestBody PagoDTO dto) {
        log.info("Iniciando actualizacion de pago con id: {}", id);
        ResponseEntity<Pago> resultado = pagoService.actualizar(id, dto)
                .map(p -> {
                    log.info("Pago actualizado exitosamente con id: {}", p.getId());
                    return ResponseEntity.ok(p);
                })
                .orElseGet(() -> {
                    log.warn("Pago no encontrado para actualizar con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Finalizada actualizacion de pago con id: {}", id);
        return resultado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Iniciando eliminacion de pago con id: {}", id);
        pagoService.eliminar(id);
        log.info("Pago eliminado exitosamente con id: {}", id);
        return ResponseEntity.noContent().build();
    }
}