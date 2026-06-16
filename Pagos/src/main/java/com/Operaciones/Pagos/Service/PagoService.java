package com.Operaciones.Pagos.Service;

import com.Operaciones.Pagos.Model.Pago;
import com.Operaciones.Pagos.Repository.PagoRepository;
import com.Operaciones.Pagos.dto.PagoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Optional<Pago> actualizar(Long id, PagoDTO dto) {
        return pagoRepository.findById(id).map(pago -> {
            pago.setVentaId(dto.getVentaId());
            pago.setMonto(dto.getMonto());
            pago.setMetodoPago(dto.getMetodoPago());
            pago.setEstado(dto.getEstado());
            return pagoRepository.save(pago);
        });
    }

    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }
}