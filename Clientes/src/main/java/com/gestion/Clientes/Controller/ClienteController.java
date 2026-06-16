package com.gestion.Clientes.Controller;

import com.gestion.Clientes.Model.Cliente;
import com.gestion.Clientes.Service.ClienteService;
import com.gestion.Clientes.dto.ClienteDTO;
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
@RequestMapping("/api/clientes")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public CollectionModel<EntityModel<Cliente>> listar() {
        log.info("Iniciando listado de clientes");
        List<EntityModel<Cliente>> clientes = clienteService.listarTodos().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClienteController.class).buscarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(ClienteController.class).listar()).withRel("clientes")))
                .collect(Collectors.toList());
        log.info("Finalizado listado de clientes - {} registros encontrados", clientes.size());
        return CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).listar()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> buscarPorId(@PathVariable Long id) {
        log.info("Iniciando busqueda de cliente por id: {}", id);
        return clienteService.buscarPorId(id)
                .map(c -> {
                    log.info("Cliente encontrado: {}", c.getId());
                    return ResponseEntity.ok(EntityModel.of(c,
                            linkTo(methodOn(ClienteController.class).buscarPorId(c.getId())).withSelfRel(),
                            linkTo(methodOn(ClienteController.class).listar()).withRel("clientes")));
                })
                .orElseGet(() -> {
                    log.warn("Cliente no encontrado con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody ClienteDTO dto) {
        log.info("Iniciando creacion de cliente: {}", dto.getNombre());
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        Cliente guardado = clienteService.guardar(cliente);
        log.info("Cliente creado exitosamente con id: {}", guardado.getId());
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        log.info("Iniciando actualizacion de cliente con id: {}", id);
        ResponseEntity<Cliente> resultado = clienteService.actualizar(id, dto)
                .map(c -> {
                    log.info("Cliente actualizado exitosamente con id: {}", c.getId());
                    return ResponseEntity.ok(c);
                })
                .orElseGet(() -> {
                    log.warn("Cliente no encontrado para actualizar con id: {}", id);
                    return ResponseEntity.notFound().build();
                });
        log.info("Finalizada actualizacion de cliente con id: {}", id);
        return resultado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Iniciando eliminacion de cliente con id: {}", id);
        clienteService.eliminar(id);
        log.info("Cliente eliminado exitosamente con id: {}", id);
        return ResponseEntity.noContent().build();
    }
} 
