package com.Operaciones.Reportes.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.Operaciones.Reportes.Model.Reporte;
import com.Operaciones.Reportes.Service.ReporteService;
import com.Operaciones.Reportes.dto.ReporteDTO;
import com.Operaciones.Reportes.security.JwtUtil;
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

@WebMvcTest(value = ReporteController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReporteService reporteService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Reporte reporte;

    @BeforeEach
    void setUp() {
        reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo("Ventas");
        reporte.setDescripcion("Reporte mensual");
    }

    @Test
    @DisplayName("GET /api/reportes - lista con HATEOAS")
    void listar_debeRetornarLista() throws Exception {
        when(reporteService.listarTodos()).thenReturn(Arrays.asList(reporte));
        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].tipo").value("Ventas"));
    }

    @Test
    @DisplayName("GET /api/reportes - lista vacia con HATEOAS")
    void listar_debeRetornarListaVacia() throws Exception {
        when(reporteService.listarTodos()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/reportes/{id} - encontrado con HATEOAS")
    void buscarPorId_debeRetornar() throws Exception {
        when(reporteService.buscarPorId(1L)).thenReturn(Optional.of(reporte));
        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Ventas"))
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/reportes/{id} - 404")
    void buscarPorId_debeRetornar404() throws Exception {
        when(reporteService.buscarPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/reportes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/reportes - crear")
    void crear_debeCrear() throws Exception {
        ReporteDTO dto = new ReporteDTO();
        dto.setTipo("Ventas");
        dto.setDescripcion("Reporte mensual");

        when(reporteService.guardar(any(Reporte.class))).thenReturn(reporte);

        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/reportes - 400 sin tipo")
    void crear_debeRetornar400() throws Exception {
        ReporteDTO dto = new ReporteDTO();
        dto.setDescripcion("Reporte mensual");

        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/reportes/{id} - actualizar")
    void actualizar_debeActualizar() throws Exception {
        ReporteDTO dto = new ReporteDTO();
        dto.setTipo("Compras");
        dto.setDescripcion("Trimestral");

        Reporte actualizado = new Reporte();
        actualizado.setId(1L);
        actualizado.setTipo("Compras");

        when(reporteService.actualizar(eq(1L), any(ReporteDTO.class)))
                .thenReturn(Optional.of(actualizado));

        mockMvc.perform(put("/api/reportes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Compras"));
    }

    @Test
    @DisplayName("PUT /api/reportes/{id} - 404")
    void actualizar_debeRetornar404() throws Exception {
        ReporteDTO dto = new ReporteDTO();
        dto.setTipo("Compras");
        dto.setDescripcion("Trimestral");

        when(reporteService.actualizar(eq(99L), any(ReporteDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/reportes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/reportes/{id}")
    void eliminar_debeEliminar() throws Exception {
        doNothing().when(reporteService).eliminar(1L);
        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/reportes/{id} - 204")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(reporteService).eliminar(99L);
        mockMvc.perform(delete("/api/reportes/99"))
                .andExpect(status().isNoContent());
    }
}
