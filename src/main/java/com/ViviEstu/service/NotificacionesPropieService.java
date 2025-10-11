package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;

import com.ViviEstu.mapper.NotificacionesPropieMapper;
import com.ViviEstu.model.dto.request.NotificacionesPropieRequestDTO;
import com.ViviEstu.model.dto.response.NotificacionesPropieResponseDTO;
import com.ViviEstu.model.entity.NotificacionPropie;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.repository.NotificacionesPropieRepository;

import com.ViviEstu.repository.PropietariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificacionesPropieService {

    private final NotificacionesPropieRepository repository;
    private final PropietariosRepository propietariosRepository;
    private final NotificacionesPropieMapper mapper;

    @Transactional(readOnly = true)
    public List<NotificacionesPropieResponseDTO> getAllNotificaciones() {
        return mapper.convertToListDTO(repository.findAll());
    }

    @Transactional(readOnly = true)
    public List<NotificacionesPropieResponseDTO> getNotificacionesByPropietarioId(Long propietarioId) {
        if (!propietariosRepository.existsById(propietarioId)) {
            throw new ResourceNotFoundException("Propietario no encontrado con id: " + propietarioId);
        }
        return mapper.convertToListDTO(repository.findByPropietario_Id(propietarioId));
    }

    @Transactional
    public NotificacionesPropieResponseDTO createNotificacion(NotificacionesPropieRequestDTO dto) {
        Propietarios propietario = propietariosRepository.findById(dto.getPropietarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con id: " + dto.getPropietarioId()));

        NotificacionPropie notificacion = new NotificacionPropie();
        notificacion.setPropietario(propietario);
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setFecha(LocalDateTime.now());

        NotificacionPropie saved = repository.save(notificacion);
        return mapper.convertToDTO(saved);
    }

    @Transactional
    public void deleteNotificacion(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Notificación no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }
}
