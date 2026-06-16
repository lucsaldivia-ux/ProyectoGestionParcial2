package com.Operaciones.Notificaciones.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.Operaciones.Notificaciones.Model.Notificacion;
import com.Operaciones.Notificaciones.Service.NotificacionService;
import com.Operaciones.Notificaciones.dto.NotificacionDTO;
import com.Operaciones.Notificaciones.security.JwtUtil;
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

@WebMvcTest(value = NotificacionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionService notificacionService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setTitulo("Bienvenido");
        notificacion.setMensaje("Gracias");
        notificacion.setDestinatario("user@mail.com");
        notificacion.setEstado("Enviado");
    }

    @Test
    @DisplayName("GET /api/notificaciones - lista con HATEOAS")
    void listar_debeRetornarLista() throws Exception {
        when(notificacionService.listarTodos()).thenReturn(Arrays.asList(notificacion));
        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.*[0].titulo").value("Bienvenido"));
    }

    @Test
    @DisplayName("GET /api/notificaciones - lista vacia con HATEOAS")
    void listar_debeRetornarListaVacia() throws Exception {
        when(notificacionService.listarTodos()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/notificaciones/{id} - encontrada con HATEOAS")
    void buscarPorId_debeRetornar() throws Exception {
        when(notificacionService.buscarPorId(1L)).thenReturn(Optional.of(notificacion));
        mockMvc.perform(get("/api/notificaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Bienvenido"))
                .andExpect(jsonPath("$._links").exists());
    }

    @Test
    @DisplayName("GET /api/notificaciones/{id} - 404")
    void buscarPorId_debeRetornar404() throws Exception {
        when(notificacionService.buscarPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/notificaciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/notificaciones - crear")
    void crear_debeCrear() throws Exception {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setTitulo("Bienvenido");
        dto.setMensaje("Gracias");
        dto.setDestinatario("user@mail.com");
        dto.setEstado("Enviado");

        when(notificacionService.guardar(any(Notificacion.class))).thenReturn(notificacion);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("POST /api/notificaciones - 400 sin titulo")
    void crear_debeRetornar400() throws Exception {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setMensaje("Gracias");
        dto.setDestinatario("user@mail.com");
        dto.setEstado("Enviado");

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/notificaciones/{id} - actualizar")
    void actualizar_debeActualizar() throws Exception {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setTitulo("Oferta");
        dto.setMensaje("Descuento");
        dto.setDestinatario("cli@mail.com");
        dto.setEstado("Pendiente");

        Notificacion actualizada = new Notificacion();
        actualizada.setId(1L);
        actualizada.setTitulo("Oferta");

        when(notificacionService.actualizar(eq(1L), any(NotificacionDTO.class)))
                .thenReturn(Optional.of(actualizada));

        mockMvc.perform(put("/api/notificaciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Oferta"));
    }

    @Test
    @DisplayName("PUT /api/notificaciones/{id} - 404")
    void actualizar_debeRetornar404() throws Exception {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setTitulo("Oferta");
        dto.setMensaje("Descuento");
        dto.setDestinatario("cli@mail.com");
        dto.setEstado("Pendiente");

        when(notificacionService.actualizar(eq(99L), any(NotificacionDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/notificaciones/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/notificaciones/{id}")
    void eliminar_debeEliminar() throws Exception {
        doNothing().when(notificacionService).eliminar(1L);
        mockMvc.perform(delete("/api/notificaciones/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/notificaciones/{id} - 204")
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(notificacionService).eliminar(99L);
        mockMvc.perform(delete("/api/notificaciones/99"))
                .andExpect(status().isNoContent());
    }
}
