package com.Operaciones.Pagos.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.Operaciones.Pagos.Model.Pago;
import com.Operaciones.Pagos.Service.PagoService;
import com.Operaciones.Pagos.dto.PagoDTO;
import com.Operaciones.Pagos.security.JwtUtil;
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

@WebMvcTest(value = PagoController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Pago pago;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setId(1L);
        pago.setVentaId(10L);
        pago.setMonto(500.0);
        pago.setMetodoPago("Tarjeta");
        pago.setEstado("Completado");
    }

    @Test
    @DisplayName("GET /api/pagos - debe retornar lista con HATEOAS")
    void listar_debeRetornarLista() throws Exception {
        when(pagoService.listarTodos()).thenReturn(Arrays.asList(pago));
        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].metodoPago").value("Tarjeta"));
    }

    @Test
    @DisplayName("GET /api/pagos - debe retornar lista vacia con HATEOAS")
    void listar_debeRetornarListaVacia() throws Exception {
        when(pagoService.listarTodos()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/pagos/{id} - debe retornar pago con HATEOAS")
    void buscarPorId_debeRetornarPago() throws Exception {
        when(pagoService.buscarPorId(1L)).thenReturn(Optional.of(pago));
        mockMvc.perform(get("/api/pagos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/pagos/{id} - debe retornar 404")
    void buscarPorId_debeRetornar404() throws Exception {
        when(pagoService.buscarPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/pagos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/pagos - debe crear pago")
    void crear_debeCrear() throws Exception {
        PagoDTO dto = new PagoDTO();
        dto.setVentaId(10L);
        dto.setMonto(500.0);
        dto.setMetodoPago("Tarjeta");
        dto.setEstado("Completado");

        when(pagoService.guardar(any(Pago.class))).thenReturn(pago);

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/pagos - debe retornar 400 si falta monto")
    void crear_debeRetornar400() throws Exception {
        PagoDTO dto = new PagoDTO();
        dto.setVentaId(10L);
        dto.setMetodoPago("Tarjeta");
        dto.setEstado("Completado");

        mockMvc.perform(post("/api/pagos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/pagos/{id} - debe actualizar pago")
    void actualizar_debeActualizar() throws Exception {
        PagoDTO dto = new PagoDTO();
        dto.setVentaId(10L);
        dto.setMonto(999.0);
        dto.setMetodoPago("Efectivo");
        dto.setEstado("Pendiente");

        Pago actualizado = new Pago();
        actualizado.setId(1L);
        actualizado.setMonto(999.0);

        when(pagoService.actualizar(eq(1L), any(PagoDTO.class)))
                .thenReturn(Optional.of(actualizado));

        mockMvc.perform(put("/api/pagos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(999.0));
    }

    @Test
    @DisplayName("PUT /api/pagos/{id} - debe retornar 404")
    void actualizar_debeRetornar404() throws Exception {
        PagoDTO dto = new PagoDTO();
        dto.setVentaId(10L);
        dto.setMonto(999.0);
        dto.setMetodoPago("Efectivo");
        dto.setEstado("Pendiente");

        when(pagoService.actualizar(eq(99L), any(PagoDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/pagos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/pagos/{id} - debe eliminar")
    void eliminar_debeEliminar() throws Exception {
        doNothing().when(pagoService).eliminar(1L);
        mockMvc.perform(delete("/api/pagos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/pagos/{id} - debe retornar 204")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(pagoService).eliminar(99L);
        mockMvc.perform(delete("/api/pagos/99"))
                .andExpect(status().isNoContent());
    }
}
