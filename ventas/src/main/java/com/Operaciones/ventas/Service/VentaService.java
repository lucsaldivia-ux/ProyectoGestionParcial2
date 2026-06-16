package com.Operaciones.ventas.Service;

import com.Operaciones.ventas.Model.Venta;
import com.Operaciones.ventas.Repository.VentaRepository;
import com.Operaciones.ventas.dto.VentaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> buscarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    public Optional<Venta> actualizar(Long id, VentaDTO dto) {
        return ventaRepository.findById(id).map(venta -> {
            venta.setClienteId(dto.getClienteId());
            venta.setProducto(dto.getProducto());
            venta.setCantidad(dto.getCantidad());
            venta.setPrecioUnitario(dto.getPrecioUnitario());
            return ventaRepository.save(venta);
        });
    }

    public void eliminar(Long id) {
        ventaRepository.deleteById(id);
    }
}