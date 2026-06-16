package com.gestion.Clientes.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.Clientes.Model.Cliente;
import com.gestion.Clientes.Service.ClienteService;
import com.gestion.Clientes.dto.ClienteDTO;
import com.gestion.Clientes.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ClienteController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setEmail("juan@mail.com");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle 123");
    }

    @Test
    @DisplayName("GET /api/clientes - debe retornar lista de clientes con HATEOAS")
    void listar_debeRetornarLista() throws Exception {
        when(clienteService.listarTodos()).thenReturn(Arrays.asList(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._embedded.*[0].nombre").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/clientes - debe retornar lista vacia con HATEOAS")
    void listar_debeRetornarListaVacia() throws Exception {
        when(clienteService.listarTodos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/clientes/{id} - debe retornar cliente con HATEOAS")
    void buscarPorId_debeRetornarCliente() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/clientes/{id} - debe retornar 404 cuando no existe")
    void buscarPorId_debeRetornar404() throws Exception {
        when(clienteService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/clientes - debe crear cliente")
    void crear_debeCrearCliente() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setEmail("juan@mail.com");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");

        when(clienteService.guardar(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/clientes - debe retornar 400 cuando falta nombre")
    void crear_debeRetornar400_SinNombre() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setApellido("Perez");
        dto.setEmail("juan@mail.com");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} - debe actualizar cliente")
    void actualizar_debeActualizarCliente() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan Updated");
        dto.setApellido("Perez");
        dto.setEmail("juan@mail.com");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");

        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("Juan Updated");
        clienteActualizado.setApellido("Perez");

        when(clienteService.actualizar(eq(1L), any(ClienteDTO.class)))
                .thenReturn(Optional.of(clienteActualizado));

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Updated"));
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} - debe retornar 404 cuando no existe")
    void actualizar_debeRetornar404() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan Updated");
        dto.setApellido("Perez");
        dto.setEmail("juan@mail.com");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");

        when(clienteService.actualizar(eq(99L), any(ClienteDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/clientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} - debe eliminar cliente")
    void eliminar_debeEliminarCliente() throws Exception {
        doNothing().when(clienteService).eliminar(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} - debe retornar 204 aunque no exista")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(clienteService).eliminar(99L);

        mockMvc.perform(delete("/api/clientes/99"))
                .andExpect(status().isNoContent());
    }
}
