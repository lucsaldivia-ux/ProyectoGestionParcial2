package com.Operaciones.Notificaciones.Service;

import com.Operaciones.Notificaciones.Model.Notificacion;
import com.Operaciones.Notificaciones.Repository.NotificacionRepository;
import com.Operaciones.Notificaciones.dto.NotificacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setTitulo("Bienvenido");
        notificacion.setMensaje("Gracias por registrarse");
        notificacion.setDestinatario("user@mail.com");
        notificacion.setEstado("Enviado");
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista")
    void listarTodos_debeRetornarLista() {
        when(notificacionRepository.findAll()).thenReturn(Arrays.asList(notificacion));
        List<Notificacion> resultado = notificacionService.listarTodos();
        assertThat(resultado).hasSize(1);
        verify(notificacionRepository).findAll();
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista vacia")
    void listarTodos_debeRetornarListaVacia() {
        when(notificacionRepository.findAll()).thenReturn(Collections.emptyList());
        List<Notificacion> resultado = notificacionService.listarTodos();
        assertThat(resultado).isEmpty();
        verify(notificacionRepository).findAll();
    }

    @Test
    @DisplayName("buscarPorId - debe retornar notificacion")
    void buscarPorId_debeRetornar() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        Optional<Notificacion> resultado = notificacionService.buscarPorId(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Bienvenido");
        verify(notificacionRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId - debe retornar empty")
    void buscarPorId_debeRetornarEmpty() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Notificacion> resultado = notificacionService.buscarPorId(99L);
        assertThat(resultado).isEmpty();
        verify(notificacionRepository).findById(99L);
    }

    @Test
    @DisplayName("guardar - debe guardar y retornar")
    void guardar_debeGuardarYRetornar() {
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);
        Notificacion resultado = notificacionService.guardar(notificacion);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    @DisplayName("actualizar - debe actualizar existente")
    void actualizar_debeActualizar() {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setTitulo("Oferta");
        dto.setMensaje("50% descuento");
        dto.setDestinatario("cli@mail.com");
        dto.setEstado("Pendiente");

        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        Optional<Notificacion> resultado = notificacionService.actualizar(1L, dto);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Oferta");
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    @DisplayName("actualizar - debe retornar empty")
    void actualizar_debeRetornarEmpty() {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setTitulo("Test");
        dto.setMensaje("Test");
        dto.setDestinatario("t@t.com");
        dto.setEstado("Nuevo");

        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Notificacion> resultado = notificacionService.actualizar(99L, dto);
        assertThat(resultado).isEmpty();
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - debe eliminar")
    void eliminar_debeEliminar() {
        doNothing().when(notificacionRepository).deleteById(1L);
        notificacionService.eliminar(1L);
        verify(notificacionRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - debe manejar id inexistente")
    void eliminar_debeManejarIdInexistente() {
        doNothing().when(notificacionRepository).deleteById(99L);
        notificacionService.eliminar(99L);
        verify(notificacionRepository).deleteById(99L);
    }
}
