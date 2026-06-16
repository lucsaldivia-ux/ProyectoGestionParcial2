package com.Operaciones.ventas.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.Operaciones.ventas.Model.Venta;
import com.Operaciones.ventas.Service.VentaService;
import com.Operaciones.ventas.dto.VentaDTO;
import com.Operaciones.ventas.security.JwtUtil;
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

@WebMvcTest(value = VentaController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService ventaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Venta venta;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(1L);
        venta.setClienteId(10L);
        venta.setProducto("Laptop");
        venta.setCantidad(2);
        venta.setPrecioUnitario(500.0);
        venta.setTotal(1000.0);
    }

    @Test
    @DisplayName("GET /api/ventas - lista con HATEOAS")
    void listar_debeRetornarLista() throws Exception {
        when(ventaService.listarTodos()).thenReturn(Arrays.asList(venta));
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].producto").value("Laptop"));
    }

    @Test
    @DisplayName("GET /api/ventas - lista vacia con HATEOAS")
    void listar_debeRetornarListaVacia() throws Exception {
        when(ventaService.listarTodos()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/ventas/{id} - encontrada con HATEOAS")
    void buscarPorId_debeRetornar() throws Exception {
        when(ventaService.buscarPorId(1L)).thenReturn(Optional.of(venta));
        mockMvc.perform(get("/api/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.producto").value("Laptop"))
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/ventas/{id} - 404")
    void buscarPorId_debeRetornar404() throws Exception {
        when(ventaService.buscarPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/ventas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/ventas - crear")
    void crear_debeCrear() throws Exception {
        VentaDTO dto = new VentaDTO();
        dto.setClienteId(10L);
        dto.setProducto("Laptop");
        dto.setCantidad(2);
        dto.setPrecioUnitario(500.0);

        when(ventaService.guardar(any(Venta.class))).thenReturn(venta);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/ventas - 400 sin producto")
    void crear_debeRetornar400() throws Exception {
        VentaDTO dto = new VentaDTO();
        dto.setClienteId(10L);
        dto.setCantidad(2);
        dto.setPrecioUnitario(500.0);

        mockMvc.perform(post("/api/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/ventas/{id} - actualizar")
    void actualizar_debeActualizar() throws Exception {
        VentaDTO dto = new VentaDTO();
        dto.setClienteId(10L);
        dto.setProducto("Monitor");
        dto.setCantidad(3);
        dto.setPrecioUnitario(300.0);

        Venta actualizada = new Venta();
        actualizada.setId(1L);
        actualizada.setProducto("Monitor");

        when(ventaService.actualizar(eq(1L), any(VentaDTO.class)))
                .thenReturn(Optional.of(actualizada));

        mockMvc.perform(put("/api/ventas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.producto").value("Monitor"));
    }

    @Test
    @DisplayName("PUT /api/ventas/{id} - 404")
    void actualizar_debeRetornar404() throws Exception {
        VentaDTO dto = new VentaDTO();
        dto.setClienteId(10L);
        dto.setProducto("Monitor");
        dto.setCantidad(3);
        dto.setPrecioUnitario(300.0);

        when(ventaService.actualizar(eq(99L), any(VentaDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/ventas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/ventas/{id}")
    void eliminar_debeEliminar() throws Exception {
        doNothing().when(ventaService).eliminar(1L);
        mockMvc.perform(delete("/api/ventas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/ventas/{id} - 204")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(ventaService).eliminar(99L);
        mockMvc.perform(delete("/api/ventas/99"))
                .andExpect(status().isNoContent());
    }
}
