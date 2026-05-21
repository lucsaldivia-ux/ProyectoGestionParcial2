package com.gestion.auditoria.Service;

import com.gestion.auditoria.DTO.AuditoriaDTO;
import com.gestion.auditoria.Model.Auditoria;
import com.gestion.auditoria.Repository.AuditoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository repo;

    public List<Auditoria> obtenerTodas() {
        return repo.findAll();
    }

    public Optional<Auditoria> obtenerPorId(Integer id) {
        return repo.findById(id);
    }

    public List<Auditoria> obtenerPorTabla(String tabla) {
        return repo.findByTabla(tabla);
    }

    public List<Auditoria> obtenerPorUsuario(String usuario) {
        return repo.findByUsuario(usuario);
    }

    public Auditoria registrar(AuditoriaDTO dto) {
        Auditoria a = new Auditoria();
        a.setAccion(dto.getAccion());
        a.setTabla(dto.getTabla());
        a.setRegistroId(dto.getRegistroId());
        a.setUsuario(dto.getUsuario());
        a.setFechaHora(LocalDateTime.now());
        a.setDetalles(dto.getDetalles());
        return repo.save(a);
    }
}
