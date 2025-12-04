package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.DatosUniversitariosMapper;
import com.ViviEstu.model.dto.request.DatosUniversitariosRequestDTO;
import com.ViviEstu.model.dto.response.DatosUniversitariosResponseDTO;
import com.ViviEstu.model.entity.DatosUniversitarios;
import com.ViviEstu.repository.DatosUniversitariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DatosUniversitariosService {

    private final DatosUniversitariosRepository datosUniversitariosRepository;
    private final DatosUniversitariosMapper mapper;

    @Transactional(readOnly = true)
    public List<DatosUniversitariosResponseDTO> getAllDatosUniversitarios() {
        return mapper.convertToListDTO(datosUniversitariosRepository.findAll());
    }

    @Transactional(readOnly = true)
    public DatosUniversitariosResponseDTO getDatosUniversitariosById(Integer id) {
        DatosUniversitarios datos = datosUniversitariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro Universitario no encontrado con id: " + id));
        return mapper.convertToDTO(datos);
    }

    @Transactional
    public DatosUniversitariosResponseDTO crearDatosUniversitarios(DatosUniversitariosRequestDTO dto) {
        if (datosUniversitariosRepository.existsByCorreoInstitucional(dto.getCorreoInstitucional())) {
            throw new DuplicateResourceException("El correo institucional ya está registrado.");
        }

        DatosUniversitarios datos = mapper.convertToEntity(dto);
        datos = datosUniversitariosRepository.save(datos);
        return mapper.convertToDTO(datos);
    }

    @Transactional
    public DatosUniversitariosResponseDTO updateDatosUniversitarios(Integer id, DatosUniversitariosRequestDTO dto) {
        DatosUniversitarios datos = datosUniversitariosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro Universitario no encontrado con id: " + id));

        // Validación de duplicidad si el correo se modifica
        if (!datos.getCorreoInstitucional().equals(dto.getCorreoInstitucional())) {
            if (datosUniversitariosRepository.existsByCorreoInstitucional(dto.getCorreoInstitucional())) {
                throw new DuplicateResourceException("El nuevo correo institucional ya está registrado.");
            }
        }

        datos.setCorreoInstitucional(dto.getCorreoInstitucional());

        return mapper.convertToDTO(datosUniversitariosRepository.save(datos));
    }

    @Transactional
    public void deleteDatosUniversitarios(Integer id) {
        if (!datosUniversitariosRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registro Universitario no encontrado con id: " + id);
        }
        datosUniversitariosRepository.deleteById(id);
    }
}