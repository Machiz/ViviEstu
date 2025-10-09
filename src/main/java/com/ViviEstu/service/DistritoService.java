package com.ViviEstu.service;

import com.upc.viviestu.mapper.DistritoMapper;
import com.upc.viviestu.repository.DistritoRepository;
import com.upc.viviestu.model.dto.DistritoRequestDTO;
import com.upc.viviestu.model.dto.DistritoResponseDTO;
import com.upc.viviestu.model.entity.Distrito;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DistritoService {

    private final DistritoRepository distritoRepository;
    private final DistritoMapper distritoMapper;


    /**
     * Crea un nuevo distrito en la base de datos.
     * @param distritoRequestDTO Datos del distrito a crear.
     * @return El distrito creado.
     */
    @Transactional
    public DistritoResponseDTO createDistrito(DistritoRequestDTO distritoRequestDTO){
        Distrito distrito = distritoMapper.convertToEntity(distritoRequestDTO);
        distritoRepository.save(distrito);
        return distritoMapper.convertToDTO(distrito);
    }

    /**
     * Obtiene una lista de todos los distritos.
     * @return Lista de todos los distritos.
     */
    @Transactional(readOnly = true)
    public List<DistritoResponseDTO> listAll(){
        return distritoMapper.convertListToDTO(distritoRepository.findAll());
    }

    /**
     * Búsqueda de distritos por nombre (zona).
     * @param nombre Nombre o parte del nombre del distrito a buscar.
     * @return Lista de distritos que coinciden con el criterio de búsqueda.
     */
    public List<DistritoResponseDTO> searchByNombre(String nombre){
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no debe ser nulo o vacío.");
        }
        return distritoMapper.convertListToDTO(distritoRepository.findByNombreContainingIgnoreCase(nombre));
    }

    /**
     * Filtrado de distritos por un rango de precios.
     * @param precioMin Precio mínimo del rango.
     * @param precioMax Precio máximo del rango.
     * @return Lista de distritos dentro del rango de precios.
     * @throws IllegalArgumentException si el precio mínimo es mayor que el precio máximo.
     */
    public List<DistritoResponseDTO> filterByPrecio(Integer precioMin, Integer precioMax){
        if (precioMin == null || precioMax == null) {
            throw new IllegalArgumentException("Los precios no deben ser nulos.");
        }
        if(precioMin >= precioMax){
            throw new IllegalArgumentException("El precio mínimo no debe ser mayor que el precio máximo.");
        }
        return distritoMapper.convertListToDTO(distritoRepository.findByPrecioPromBetween(precioMin, precioMax));
    }

    /**
     * Filtrado de distritos por tipo de alojamiento.
     * @param tipo Tipo de alojamiento a filtrar.
     * @return Lista de distritos que ofrecen ese tipo de alojamiento.
     */
    public List<DistritoResponseDTO> filterByTipo(String tipo){
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo no debe ser nulo o vacío.");
        }
        return distritoMapper.convertListToDTO(distritoRepository.findByTipo(tipo));
    }
}
