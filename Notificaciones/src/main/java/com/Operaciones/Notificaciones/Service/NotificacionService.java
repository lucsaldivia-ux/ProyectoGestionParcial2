package com.Operaciones.Notificaciones.Service;

import com.Operaciones.Notificaciones.Model.Notificacion;
import com.Operaciones.Notificaciones.Repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public List<Notificacion> listarTodos() {
        return notificacionRepository.findAll();
    }

    public Optional<Notificacion> buscarPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    public Notificacion guardar(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    public void eliminar(Long id) {
        notificacionRepository.deleteById(id);
    }
}