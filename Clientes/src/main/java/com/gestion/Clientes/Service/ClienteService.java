package com.gestion.Clientes.Service;

import com.gestion.Clientes.Model.Cliente;
import com.gestion.Clientes.Repository.ClienteRepository;
import com.gestion.Clientes.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> actualizar(Long id, ClienteDTO dto) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(dto.getNombre());
            cliente.setApellido(dto.getApellido());
            cliente.setEmail(dto.getEmail());
            cliente.setTelefono(dto.getTelefono());
            cliente.setDireccion(dto.getDireccion());
            return clienteRepository.save(cliente);
        });
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
} 
