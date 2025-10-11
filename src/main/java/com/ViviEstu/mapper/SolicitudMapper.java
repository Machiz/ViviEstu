package com.ViviEstu.mapper;
import com.ViviEstu.model.dto.request.SolicitudRequestDTO;
import com.ViviEstu.model.dto.response.SolicitudResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Solicitudes;
import org.springframework.stereotype.Component;

@Component
public class SolicitudMapper {
    public Solicitudes toEntity(SolicitudRequestDTO dto, Estudiantes estudiante, Alojamiento alojamiento) {
        Solicitudes solicitud = new Solicitudes();
        solicitud.setMesesAlquiler(dto.getMesesAlquiler());
        solicitud.setCantInquilinos(dto.getCantInquilinos());
        solicitud.setMensaje(dto.getMensaje());
        solicitud.setOferta(dto.getOferta());
        solicitud.setEstudiantes(estudiante);
        solicitud.setAlojamiento(alojamiento);
        return solicitud;
    }

    public SolicitudResponseDTO toDTO(Solicitudes entity) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(entity.getId());
        dto.setMesesAlquiler(entity.getMesesAlquiler());
        dto.setCantInquilinos(entity.getCantInquilinos());
        dto.setMensaje(entity.getMensaje());
        dto.setOferta(entity.getOferta());

        if (entity.getEstudiantes() != null) {
            dto.setEstudiantesId(entity.getEstudiantes().getId());
            dto.setNombreEstudiante(entity.getEstudiantes().getNombre());
        }
        if (entity.getAlojamiento() != null) {
            dto.setAlojamientoId(entity.getAlojamiento().getId());
            dto.setTituloAlojamiento(entity.getAlojamiento().getTitulo());
        }
        return dto;
    }
}
