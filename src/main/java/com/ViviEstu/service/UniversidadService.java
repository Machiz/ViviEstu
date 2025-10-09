package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.UniversidadMapper;
import com.ViviEstu.model.dto.request.UniversidadRequestDTO;
import com.ViviEstu.model.dto.response.UniversidadResponseDTO;
import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.model.entity.Universidad;
import com.ViviEstu.repository.DistritoRepository;
import com.ViviEstu.repository.UniversidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversidadService {
    private final UniversidadRepository universidadRepository;
    private final DistritoRepository distritoRepository;

    public UniversidadService(UniversidadRepository universidadRepository, DistritoRepository distritoRepository) {
        this.universidadRepository = universidadRepository;
        this.distritoRepository = distritoRepository;
    }

    public UniversidadResponseDTO crear(UniversidadRequestDTO request) {
        Distrito distrito = distritoRepository.findById(request.getDistritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con id " + request.getDistritoId()));

        Universidad u = new Universidad();
        u.setNombre(request.getNombre());
        u.setDistrito(distrito);

        Universidad guardada = universidadRepository.save(u);
        return UniversidadMapper.toResponse(guardada);
    }

    public List<UniversidadResponseDTO> listar() {
        return universidadRepository.findAll().stream()
                .map(UniversidadMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UniversidadResponseDTO obtenerPorId(Long id) {
        Universidad u = universidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con id " + id));
        return UniversidadMapper.toResponse(u);
    }

    public List<UniversidadResponseDTO> buscarPorNombre(String nombre) {
        return universidadRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(UniversidadMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UniversidadResponseDTO actualizar(Long id, UniversidadRequestDTO request) {
        Universidad u = universidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con id " + id));

        // actualizar nombre
        u.setNombre(request.getNombre());

        // si cambiaron el distrito lo resolvemos
        if (!u.getDistrito().getId().equals(request.getDistritoId())) {
            Distrito d = distritoRepository.findById(request.getDistritoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con id " + request.getDistritoId()));
            u.setDistrito(d);
        }

        Universidad actualizada = universidadRepository.save(u);
        return UniversidadMapper.toResponse(actualizada);
    }

    public void eliminar(Long id) {
        if (!universidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Universidad no encontrada con id " + id);
        }
        universidadRepository.deleteById(id);
    }
}
