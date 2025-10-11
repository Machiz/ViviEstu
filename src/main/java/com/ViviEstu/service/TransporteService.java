package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.TransporteMapper;
import com.ViviEstu.model.dto.request.TransporteRequestDTO;
import com.ViviEstu.model.dto.response.TransporteResponseDTO;
import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.model.entity.Transporte;
import com.ViviEstu.repository.DistritoRepository;
import com.ViviEstu.repository.TransporteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransporteService {

    private final TransporteRepository TransporteRepository;
    private final DistritoRepository zonaRepository;
    private final TransporteMapper mapper;

    @Transactional
    public TransporteResponseDTO crear(TransporteRequestDTO dto) {
        Distrito distrito = zonaRepository.findById(dto.getZonaId())
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada"));

        Transporte medio = new Transporte();
        medio.setNombre(dto.getNombre());
        medio.setDistrito(distrito);

        return mapper.convertToDTO(TransporteRepository.save(medio));
    }

    public List<TransporteResponseDTO> listar() {
        return TransporteRepository.findAll()
                .stream().map(mapper::convertToDTO).toList();
    }

    public TransporteResponseDTO obtenerPorId(Long id) {
        Transporte medio = TransporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medio de transporte no encontrado"));
        return mapper.convertToDTO(medio);
    }

    @Transactional
    public void eliminar(Long id) {
        Transporte medio = TransporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medio de transporte no encontrado"));
        TransporteRepository.delete(medio);
    }
}
