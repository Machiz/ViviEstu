package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.AlojamientoMapper;
import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;
    private final DistritoRepository distritoRepository;
    private final PropietariosRepository propietariosRepository;
    private final ImagenesRepository imagenesRepository;
    private final AlojamientoMapper mapper;
    private final GeocodingService geocodingService;
    private final DatosPropiedadesRepository datosPropiedadesRepository;
    private final TransporteRepository transporteRepository;
    private final UniversidadRepository universidadRepository;
    private final UniAlojamientoRepository uniAlojamientoRepository;
    private final CloudinaryService cloudinaryService;

    /* ============================================================
       US-017 → Búsqueda de alojamientos por distrito/universidad
       ============================================================ */
    @Transactional(readOnly = true)
    public List<AlojamientoResponseDTO> getAllAlojamientos() {
        return mapper.convertToListDTO(alojamientoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public AlojamientoResponseDTO getAlojamientoById(Long id) {
        Alojamiento alojamiento = alojamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + id));
        return mapper.convertToDTO(alojamiento);
    }

    @Transactional(readOnly = true)
    public List<AlojamientoResponseDTO> listarPorDistrito(Long distritoId) {
        List<Alojamiento> alojamientos = alojamientoRepository.findByDistritoId(distritoId);
        if (alojamientos.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron alojamientos en este distrito. Intenta con otro cercano.");
        }
        return alojamientos.stream()
                .map(mapper::convertToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlojamientoResponseDTO> listarPorUniversidad(Long universidadId) {
        List<Alojamiento> alojamientos = alojamientoRepository.findByUniversidadId(universidadId);
        if (alojamientos.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontraron alojamientos cercanos a esta universidad.");
        }
        return alojamientos.stream()
                .map(mapper::convertToDTO)
                .toList();
    }

    /* ============================================================
       US-020 → Reportes, creación y gestión del arrendador
       ============================================================ */
    @Transactional
    public AlojamientoResponseDTO crearAlojamiento(AlojamientoRequestDTO dto) throws IOException {

        if (dto.getImagenes() == null || dto.getImagenes().isEmpty()) {
            throw new ResourceNotFoundException("Debe subir al menos una imagen para el alojamiento.");
        }

        if (dto.getDescripcion() == null || dto.getDescripcion().length() < 50) {
            throw new IllegalArgumentException("La descripción debe tener al menos 50 caracteres.");
        }

        if (dto.getPrecioMensual().compareTo(new java.math.BigDecimal("200.00")) < 0
                || dto.getPrecioMensual().compareTo(new java.math.BigDecimal("5000.00")) > 0) {
            throw new IllegalArgumentException("El precio debe estar entre S/200 y S/5000.");
        }

        if (alojamientoRepository.existsByNroPartida(dto.getNroPartida())) {
            throw new DuplicateResourceException("Alquiler ya publicado");
        }

        Distrito distrito = distritoRepository.findById(dto.getDistritoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Distrito no encontrado con id: " + dto.getDistritoId()));

        Propietarios propietario = propietariosRepository.findById(dto.getPropietarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Propietario no encontrado con id: " + dto.getPropietarioId()));

        if (alojamientoRepository.countByPropietarioIdAndAlquiladoIsFalse(propietario.getId()) >= 20) {
            throw new IllegalStateException("Ha alcanzado el límite máximo de 20 ofertas activas.");
        }

        if (!datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(propietario.getDni(), dto.getNroPartida())) {
            throw new ResourceNotFoundException("Datos no encontrados en base de datos");
        }

        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setTitulo(dto.getTitulo());
        alojamiento.setDescripcion(dto.getDescripcion());
        alojamiento.setDireccion(dto.getDireccion());
        alojamiento.setPrecioMensual(dto.getPrecioMensual());
        alojamiento.setFecha(new Timestamp(System.currentTimeMillis()));
        alojamiento.setAlquilado(false);
        alojamiento.setNroPartida(dto.getNroPartida());
        alojamiento.setDistrito(distrito);
        alojamiento.setPropietario(propietario);

        alojamiento = alojamientoRepository.save(alojamiento);

        // Guardar imágenes
        for (MultipartFile imagen : dto.getImagenes()) {
            Map uploadResult = cloudinaryService.subirImagen(imagen);
            ImagenesAlojamiento img = new ImagenesAlojamiento();
            img.setUrl(uploadResult.get("secure_url").toString());
            img.setPublicId(uploadResult.get("public_id").toString());
            img.setAlojamiento(alojamiento);
            imagenesRepository.save(img);
        }

        // Guardar transportes asociados
        if (dto.getTransportes() != null && !dto.getTransportes().isEmpty()) {
            List<Transporte> transportes = new ArrayList<>();
            for (String nombre : dto.getTransportes()) {
                Transporte t = new Transporte();
                t.setNombre(nombre);
                t.setAlojamiento(alojamiento);
                transportes.add(t);
            }
            transporteRepository.saveAll(transportes);
        }

        // Guardar universidades asociadas
        if (dto.getUniversidadesIds() != null && !dto.getUniversidadesIds().isEmpty()) {
            List<UniAlojamiento> relaciones = new ArrayList<>();
            for (Long universidadId : dto.getUniversidadesIds()) {
                Universidad universidad = universidadRepository.findById(universidadId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Universidad no encontrada con id: " + universidadId));
                UniAlojamiento relacion = new UniAlojamiento();
                relacion.setAlojamiento(alojamiento);
                relacion.setUniversidad(universidad);
                relaciones.add(relacion);
            }
            uniAlojamientoRepository.saveAll(relaciones);
        }

        return mapper.convertToDTO(alojamiento);
    }

    @Transactional
    public AlojamientoResponseDTO updateAlojamiento(Long id, AlojamientoRequestDTO dto) {
        Alojamiento alojamiento = alojamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + id));

        if (dto.getDistritoId() != null && !dto.getDistritoId().equals(alojamiento.getDistrito().getId())) {
            throw new IllegalArgumentException("No se puede cambiar el distrito del alojamiento.");
        }

        if (dto.getPropietarioId() != null && !dto.getPropietarioId().equals(alojamiento.getPropietario().getId())) {
            throw new IllegalArgumentException("No se puede cambiar el propietario del alojamiento.");
        }

        alojamiento.setTitulo(dto.getTitulo());
        alojamiento.setDescripcion(dto.getDescripcion());
        alojamiento.setDireccion(dto.getDireccion());
        alojamiento.setPrecioMensual(dto.getPrecioMensual());
        alojamiento.setAlquilado(dto.getAlquilado());

        return mapper.convertToDTO(alojamientoRepository.save(alojamiento));
    }

    @Transactional
    public AlojamientoResponseDTO agregarImagenes(Long alojamientoId, List<MultipartFile> nuevasImagenes) throws IOException {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + alojamientoId));

        if (nuevasImagenes == null || nuevasImagenes.isEmpty()) {
            throw new IllegalArgumentException("Debe subir al menos una imagen para agregar.");
        }

        for (MultipartFile imagen : nuevasImagenes) {
            Map uploadResult = cloudinaryService.subirImagen(imagen);
            ImagenesAlojamiento img = new ImagenesAlojamiento();
            img.setUrl(uploadResult.get("secure_url").toString());
            img.setPublicId(uploadResult.get("public_id").toString());
            img.setAlojamiento(alojamiento);
            imagenesRepository.save(img);
        }

        return mapper.convertToDTO(alojamiento);
    }

    @Transactional
    public AlojamientoResponseDTO marcarComoAlquilado(Long id) {
        Alojamiento alojamiento = alojamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + id));
        alojamiento.setAlquilado(true);
        return mapper.convertToDTO(alojamientoRepository.save(alojamiento));
    }

    /* ============================================================
       US-016 → Visualización del perfil del propietario
       ============================================================ */
    @Transactional(readOnly = true)
    public PropietariosResponseDTO obtenerDatosVendedor(Long alojamientoId) {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alojamiento no encontrado con id: " + alojamientoId));

        Propietarios propietario = alojamiento.getPropietario();
        if (propietario == null) {
            throw new ResourceNotFoundException("El alojamiento no tiene un propietario asociado.");
        }

        PropietariosResponseDTO dto = new PropietariosResponseDTO();
        dto.setId(propietario.getId());
        dto.setNombre(propietario.getNombre());
        dto.setApellidos(propietario.getApellidos());
        dto.setCorreo(propietario.getUser().getCorreo());
        dto.setTelefono(propietario.getTelefono());
        dto.setDni(propietario.getDni());
        return dto;
    }

    /* ============================================================
       Eliminación total de alojamiento e imágenes
       ============================================================ */
    @Transactional
    public void deleteAlojamiento(Long id) {
        Alojamiento alojamiento = alojamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + id));

        List<ImagenesAlojamiento> imagenes = imagenesRepository.findByAlojamientoId(id);
        for (ImagenesAlojamiento img : imagenes) {
            try {
                cloudinaryService.eliminarImagen(img.getPublicId());
            } catch (Exception e) {
                System.err.println("Error al eliminar imagen de Cloudinary: " + img.getPublicId());
            }
        }

        imagenesRepository.deleteAll(imagenes);
        transporteRepository.deleteAll(transporteRepository.findByAlojamientoId(id));
        uniAlojamientoRepository.deleteAll(uniAlojamientoRepository.findByAlojamientoId(id));

        alojamientoRepository.delete(alojamiento);
    }

    @Transactional
    public void eliminarImagen(Long alojamientoId, Long imagenId) throws IOException {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + alojamientoId));

        List<ImagenesAlojamiento> imagenes = imagenesRepository.findByAlojamientoId(alojamientoId);
        if (imagenes.size() <= 1) {
            throw new IllegalStateException("El alojamiento debe tener al menos una imagen.");
        }

        ImagenesAlojamiento imagen = imagenesRepository.findById(imagenId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imagenId));

        if (!imagen.getAlojamiento().getId().equals(alojamientoId)) {
            throw new IllegalArgumentException("La imagen no pertenece al alojamiento especificado.");
        }

        cloudinaryService.eliminarImagen(imagen.getPublicId());
        imagenesRepository.delete(imagen);
    }
}
