package com.gestion.auditoria.Service;

import com.gestion.auditoria.DTO.AuditoriaDTO;
import com.gestion.auditoria.Model.Auditoria;
import com.gestion.auditoria.Repository.AuditoriaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository repo;

    public List<Auditoria> obtenerTodas() {
        log.info("Obteniendo todos los registros de auditoría");
        List<Auditoria> registros = repo.findAll();
        log.info("Se encontraron {} registros de auditoría", registros.size());
        return registros;
    }

    public Optional<Auditoria> obtenerPorId(Integer id) {
        log.info("Buscando registro de auditoría con id={}", id);
        Optional<Auditoria> auditoria = repo.findById(id);
        if (auditoria.isPresent()) {
            log.info("Registro de auditoría encontrado: id={}", id);
        } else {
            log.warn("Registro de auditoría no encontrado: id={}", id);
        }
        return auditoria;
    }

    public List<Auditoria> obtenerPorTabla(String tabla) {
        log.info("Buscando registros de auditoría por tabla={}", tabla);
        List<Auditoria> registros = repo.findByTabla(tabla);
        log.info("Se encontraron {} registros para tabla={}", registros.size(), tabla);
        return registros;
    }

    public List<Auditoria> obtenerPorUsuario(String usuario) {
        log.info("Buscando registros de auditoría por usuario={}", usuario);
        List<Auditoria> registros = repo.findByUsuario(usuario);
        log.info("Se encontraron {} registros para usuario={}", registros.size(), usuario);
        return registros;
    }

    public Auditoria registrar(AuditoriaDTO dto) {
        log.info("Registrando auditoría: accion={} tabla={} usuario={}", dto.getAccion(), dto.getTabla(), dto.getUsuario());
        Auditoria a = new Auditoria();
        a.setAccion(dto.getAccion());
        a.setTabla(dto.getTabla());
        a.setRegistroId(dto.getRegistroId());
        a.setUsuario(dto.getUsuario());
        a.setFechaHora(LocalDateTime.now());
        a.setDetalles(dto.getDetalles());
        Auditoria guardado = repo.save(a);
        log.info("Auditoría registrada exitosamente con id={}", guardado.getId());
        return guardado;
    }
}
