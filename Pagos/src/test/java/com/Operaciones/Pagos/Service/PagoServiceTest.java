package com.Operaciones.Pagos.Service;

import com.Operaciones.Pagos.Model.Pago;
import com.Operaciones.Pagos.Repository.PagoRepository;
import com.Operaciones.Pagos.dto.PagoDTO;
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
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

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
    @DisplayName("listarTodos - debe retornar lista de pagos")
    void listarTodos_debeRetornarLista() {
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago));
        List<Pago> resultado = pagoService.listarTodos();
        assertThat(resultado).hasSize(1);
        verify(pagoRepository).findAll();
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista vacia")
    void listarTodos_debeRetornarListaVacia() {
        when(pagoRepository.findAll()).thenReturn(Collections.emptyList());
        List<Pago> resultado = pagoService.listarTodos();
        assertThat(resultado).isEmpty();
        verify(pagoRepository).findAll();
    }

    @Test
    @DisplayName("buscarPorId - debe retornar pago cuando existe")
    void buscarPorId_debeRetornarPago() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        Optional<Pago> resultado = pagoService.buscarPorId(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        verify(pagoRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId - debe retornar empty cuando no existe")
    void buscarPorId_debeRetornarEmpty() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Pago> resultado = pagoService.buscarPorId(99L);
        assertThat(resultado).isEmpty();
        verify(pagoRepository).findById(99L);
    }

    @Test
    @DisplayName("guardar - debe guardar y retornar pago")
    void guardar_debeGuardarYRetornar() {
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);
        Pago resultado = pagoService.guardar(pago);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("actualizar - debe actualizar pago existente")
    void actualizar_debeActualizar() {
        PagoDTO dto = new PagoDTO();
        dto.setVentaId(20L);
        dto.setMonto(999.0);
        dto.setMetodoPago("Efectivo");
        dto.setEstado("Pendiente");

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        Optional<Pago> resultado = pagoService.actualizar(1L, dto);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getMetodoPago()).isEqualTo("Efectivo");
        assertThat(resultado.get().getMonto()).isEqualTo(999.0);
        verify(pagoRepository).findById(1L);
        verify(pagoRepository).save(pago);
    }

    @Test
    @DisplayName("actualizar - debe retornar empty cuando no existe")
    void actualizar_debeRetornarEmpty() {
        PagoDTO dto = new PagoDTO();
        dto.setVentaId(1L);
        dto.setMonto(100.0);
        dto.setMetodoPago("Efectivo");
        dto.setEstado("Pendiente");

        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pago> resultado = pagoService.actualizar(99L, dto);
        assertThat(resultado).isEmpty();
        verify(pagoRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - debe eliminar pago")
    void eliminar_debeEliminar() {
        doNothing().when(pagoRepository).deleteById(1L);
        pagoService.eliminar(1L);
        verify(pagoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - debe manejar id inexistente")
    void eliminar_debeManejarIdInexistente() {
        doNothing().when(pagoRepository).deleteById(99L);
        pagoService.eliminar(99L);
        verify(pagoRepository).deleteById(99L);
    }
}
