package com.gestion.Clientes.Service;

import com.gestion.Clientes.Model.Cliente;
import com.gestion.Clientes.Repository.ClienteRepository;
import com.gestion.Clientes.dto.ClienteDTO;
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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

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
    @DisplayName("listarTodos - debe retornar lista de clientes")
    void listarTodos_debeRetornarListaClientes() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

        List<Cliente> resultado = clienteService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("listarTodos - debe retornar lista vacia cuando no hay clientes")
    void listarTodos_debeRetornarListaVacia() {
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        List<Cliente> resultado = clienteService.listarTodos();

        assertThat(resultado).isEmpty();
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("buscarPorId - debe retornar cliente cuando existe")
    void buscarPorId_debeRetornarCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        verify(clienteRepository).findById(1L);
    }

    @Test
    @DisplayName("buscarPorId - debe retornar empty cuando no existe")
    void buscarPorId_debeRetornarEmpty() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarPorId(99L);

        assertThat(resultado).isEmpty();
        verify(clienteRepository).findById(99L);
    }

    @Test
    @DisplayName("guardar - debe guardar y retornar el cliente")
    void guardar_debeGuardarYRetornarCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.guardar(cliente);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("guardar - debe guardar cliente sin id")
    void guardar_debeGuardarClienteSinId() {
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre("Ana");
        nuevoCliente.setApellido("Lopez");

        Cliente clienteConId = new Cliente();
        clienteConId.setId(2L);
        clienteConId.setNombre("Ana");
        clienteConId.setApellido("Lopez");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteConId);

        Cliente resultado = clienteService.guardar(nuevoCliente);

        assertThat(resultado.getId()).isEqualTo(2L);
        verify(clienteRepository).save(nuevoCliente);
    }

    @Test
    @DisplayName("actualizar - debe actualizar cliente existente")
    void actualizar_debeActualizarCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan Updated");
        dto.setApellido("Perez Updated");
        dto.setEmail("juan.updated@mail.com");
        dto.setTelefono("987654321");
        dto.setDireccion("Calle 456");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Optional<Cliente> resultado = clienteService.actualizar(1L, dto);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Juan Updated");
        assertThat(resultado.get().getApellido()).isEqualTo("Perez Updated");
        assertThat(resultado.get().getEmail()).isEqualTo("juan.updated@mail.com");
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(cliente);
    }

    @Test
    @DisplayName("actualizar - debe retornar empty cuando cliente no existe")
    void actualizar_debeRetornarEmpty() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Test");

        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.actualizar(99L, dto);

        assertThat(resultado).isEmpty();
        verify(clienteRepository).findById(99L);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("eliminar - debe eliminar cliente por id")
    void eliminar_debeEliminarCliente() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.eliminar(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - debe manejar id inexistente")
    void eliminar_debeManejarIdInexistente() {
        doNothing().when(clienteRepository).deleteById(99L);

        clienteService.eliminar(99L);

        verify(clienteRepository).deleteById(99L);
    }
}
