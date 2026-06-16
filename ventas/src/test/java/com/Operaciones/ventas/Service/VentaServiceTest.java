package com.Operaciones.ventas.Service;

import com.Operaciones.ventas.Model.Venta;
import com.Operaciones.ventas.Repository.VentaRepository;
import com.Operaciones.ventas.dto.VentaDTO;
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
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaService ventaService;

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
    @DisplayName("listarTodos - debe retornar lista")
    void listarTodos_debeRetornarLista() {
        when(ventaRepository.findAll()).thenReturn(Arrays.asList(venta));
        List<Venta> resultado = ventaService.listarTodos();
        assertThat(resultado).hasSize(1);
        verify(ventaRepository).findAll();
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista vacia")
    void listarTodos_debeRetornarListaVacia() {
        when(ventaRepository.findAll()).thenReturn(Collections.emptyList());
        List<Venta> resultado = ventaService.listarTodos();
        assertThat(resultado).isEmpty();
        verify(ventaRepository).findAll();
    }

    @Test
    @DisplayName("buscarPorId - debe retornar venta")
    void buscarPorId_debeRetornar() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        Optional<Venta> resultado = ventaService.buscarPorId(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getProducto()).isEqualTo("Laptop");
        verify(ventaRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId - debe retornar empty")
    void buscarPorId_debeRetornarEmpty() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Venta> resultado = ventaService.buscarPorId(99L);
        assertThat(resultado).isEmpty();
        verify(ventaRepository).findById(99L);
    }

    @Test
    @DisplayName("guardar - debe guardar y retornar")
    void guardar_debeGuardarYRetornar() {
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        Venta resultado = ventaService.guardar(venta);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(ventaRepository).save(venta);
    }

    @Test
    @DisplayName("actualizar - debe actualizar existente")
    void actualizar_debeActualizar() {
        VentaDTO dto = new VentaDTO();
        dto.setClienteId(20L);
        dto.setProducto("Mouse");
        dto.setCantidad(5);
        dto.setPrecioUnitario(25.0);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Optional<Venta> resultado = ventaService.actualizar(1L, dto);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getProducto()).isEqualTo("Mouse");
        assertThat(resultado.get().getCantidad()).isEqualTo(5);
        verify(ventaRepository).save(venta);
    }

    @Test
    @DisplayName("actualizar - debe retornar empty")
    void actualizar_debeRetornarEmpty() {
        VentaDTO dto = new VentaDTO();
        dto.setClienteId(1L);
        dto.setProducto("Test");
        dto.setCantidad(1);
        dto.setPrecioUnitario(10.0);

        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Venta> resultado = ventaService.actualizar(99L, dto);
        assertThat(resultado).isEmpty();
        verify(ventaRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - debe eliminar")
    void eliminar_debeEliminar() {
        doNothing().when(ventaRepository).deleteById(1L);
        ventaService.eliminar(1L);
        verify(ventaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - debe manejar id inexistente")
    void eliminar_debeManejarIdInexistente() {
        doNothing().when(ventaRepository).deleteById(99L);
        ventaService.eliminar(99L);
        verify(ventaRepository).deleteById(99L);
    }
}
