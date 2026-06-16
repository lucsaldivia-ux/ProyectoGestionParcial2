package com.Operaciones.Reportes.Service;

import com.Operaciones.Reportes.Model.Reporte;
import com.Operaciones.Reportes.Repository.ReporteRepository;
import com.Operaciones.Reportes.dto.ReporteDTO;
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
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reporte;

    @BeforeEach
    void setUp() {
        reporte = new Reporte();
        reporte.setId(1L);
        reporte.setTipo("Ventas");
        reporte.setDescripcion("Reporte mensual");
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista")
    void listarTodos_debeRetornarLista() {
        when(reporteRepository.findAll()).thenReturn(Arrays.asList(reporte));
        List<Reporte> resultado = reporteService.listarTodos();
        assertThat(resultado).hasSize(1);
        verify(reporteRepository).findAll();
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista vacia")
    void listarTodos_debeRetornarListaVacia() {
        when(reporteRepository.findAll()).thenReturn(Collections.emptyList());
        List<Reporte> resultado = reporteService.listarTodos();
        assertThat(resultado).isEmpty();
        verify(reporteRepository).findAll();
    }

    @Test
    @DisplayName("buscarPorId - debe retornar reporte")
    void buscarPorId_debeRetornar() {
        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        Optional<Reporte> resultado = reporteService.buscarPorId(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTipo()).isEqualTo("Ventas");
        verify(reporteRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId - debe retornar empty")
    void buscarPorId_debeRetornarEmpty() {
        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Reporte> resultado = reporteService.buscarPorId(99L);
        assertThat(resultado).isEmpty();
        verify(reporteRepository).findById(99L);
    }

    @Test
    @DisplayName("guardar - debe guardar y retornar")
    void guardar_debeGuardarYRetornar() {
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);
        Reporte resultado = reporteService.guardar(reporte);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(reporteRepository).save(reporte);
    }

    @Test
    @DisplayName("actualizar - debe actualizar existente")
    void actualizar_debeActualizar() {
        ReporteDTO dto = new ReporteDTO();
        dto.setTipo("Compras");
        dto.setDescripcion("Reporte trimestral");

        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte);

        Optional<Reporte> resultado = reporteService.actualizar(1L, dto);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTipo()).isEqualTo("Compras");
        verify(reporteRepository).save(reporte);
    }

    @Test
    @DisplayName("actualizar - debe retornar empty")
    void actualizar_debeRetornarEmpty() {
        ReporteDTO dto = new ReporteDTO();
        dto.setTipo("Test");
        dto.setDescripcion("Test");

        when(reporteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Reporte> resultado = reporteService.actualizar(99L, dto);
        assertThat(resultado).isEmpty();
        verify(reporteRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - debe eliminar")
    void eliminar_debeEliminar() {
        doNothing().when(reporteRepository).deleteById(1L);
        reporteService.eliminar(1L);
        verify(reporteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - debe manejar id inexistente")
    void eliminar_debeManejarIdInexistente() {
        doNothing().when(reporteRepository).deleteById(99L);
        reporteService.eliminar(99L);
        verify(reporteRepository).deleteById(99L);
    }
}
