package com.ViviEstu.service;

import com.ViviEstu.exception.NoDataFoundException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.DistritoMapper;
import com.ViviEstu.repository.DistritoRepository;
import com.ViviEstu.model.dto.request.DistritoRequestDTO;
import com.ViviEstu.model.dto.response.DistritoResponseDTO;
import com.ViviEstu.model.entity.Distrito;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DistritoService {

    private final DistritoRepository distritoRepository;
    private final DistritoMapper distritoMapper;

    @Transactional
    public DistritoResponseDTO createDistrito(DistritoRequestDTO distritoRequestDTO){
        Distrito distrito = distritoMapper.convertToEntity(distritoRequestDTO);
        distritoRepository.save(distrito);
        return distritoMapper.convertToDTO(distrito);
    }

    @Transactional(readOnly = true)
    public List<DistritoResponseDTO> listAll(){
        return distritoMapper.convertListToDTO(distritoRepository.findAll());
    }

    public List<DistritoResponseDTO> searchByNombre(String nombre){
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no debe ser nulo o vacío.");
        }
        return distritoMapper.convertListToDTO(distritoRepository.findByNombreContainingIgnoreCase(nombre));
    }


    public List<DistritoResponseDTO> filterByPrecio(Integer precioMin, Integer precioMax){
        if (precioMin == null || precioMax == null) {
            throw new IllegalArgumentException("Los precios no deben ser nulos.");
        }
        if(precioMin >= precioMax){
            throw new IllegalArgumentException("El precio mínimo no debe ser mayor que el precio máximo.");
        }
        return distritoMapper.convertListToDTO(distritoRepository.findByPrecioPromBetween(precioMin, precioMax));
    }


    public List<DistritoResponseDTO> filterByTipo(String tipo){
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo no debe ser nulo o vacío.");
        }
        return distritoMapper.convertListToDTO(distritoRepository.findByTipo(tipo));
    }

    @Transactional(readOnly = true)
    public DistritoResponseDTO getDistritoById(Long id) {
        Distrito distrito = distritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado con id: " + id));

        if ((distrito.getDescripcion() == null || distrito.getDescripcion().isEmpty()) &&
                distrito.getPrecioProm() == null) {
            throw new NoDataFoundException("Aún no tenemos información disponible para este distrito");
        }
        return distritoMapper.convertToDTO(distrito);
    }


    @Transactional
    public DistritoResponseDTO updateDistrito(Long id, DistritoRequestDTO distritoRequestDTO) {
        Distrito distrito = distritoRepository.findById(id)
                .orElseThrow(() -> new com.ViviEstu.exception.ResourceNotFoundException("Distrito no encontrado con id: " + id));
        distritoMapper.updateEntityFromDTO(distritoRequestDTO, distrito);
        distritoRepository.save(distrito);
        return distritoMapper.convertToDTO(distrito);
    }

    @Transactional
    public void deleteDistrito(Long id) {
        Distrito distrito = distritoRepository.findById(id)
                .orElseThrow(() -> new com.ViviEstu.exception.ResourceNotFoundException("Distrito no encontrado con id: " + id));
        distritoRepository.delete(distrito);
    }

}
