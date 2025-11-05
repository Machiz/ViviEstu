package com.ViviEstu.Unit;


import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.AlojamientoMapper;
import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
import com.ViviEstu.service.AlojamientoService;
import com.ViviEstu.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlojamientoServiceTest {
    @Mock
    private AlojamientoRepository alojamientoRepository;
    @Mock
    private DistritoRepository distritoRepository;
    @Mock
    private PropietariosRepository propietariosRepository;
    @Mock
    private ImagenesRepository imagenesRepository;
    @Mock
    private AlojamientoMapper mapper;
    @Mock
    private DatosPropiedadesRepository datosPropiedadesRepository;

    @Mock
    private TransporteRepository transporteRepository;

    @Mock
    private UniAlojamientoRepository uniAlojamientoRepository;

    @Mock
    private UniversidadRepository universidadRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private AlojamientoService alojamientoService;

    private AlojamientoRequestDTO alojamientoRequestDTO;
    private Propietarios propietario;
    private Distrito distrito;
    private Alojamiento alojamiento;
    private AlojamientoResponseDTO alojamientoResponseDTO;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba
        alojamientoRequestDTO = new AlojamientoRequestDTO();
        alojamientoRequestDTO.setTitulo("Título de prueba");
        alojamientoRequestDTO.setDescripcion("Esta es una descripción de más de cincuenta caracteres para la prueba.");
        alojamientoRequestDTO.setDireccion("Dirección de prueba");
        alojamientoRequestDTO.setPrecioMensual(new BigDecimal("500.00"));
        alojamientoRequestDTO.setNroPartida("12345678");
        alojamientoRequestDTO.setAlquilado(false);
        alojamientoRequestDTO.setPropietarioId(1L);
        alojamientoRequestDTO.setDistritoId(1L);
        MultipartFile mockFile = mock(MultipartFile.class);
        alojamientoRequestDTO.setImagenes(Collections.singletonList(mockFile));

        propietario = new Propietarios();
        propietario.setId(1L);
        propietario.setDni("12345678A");

        distrito = new Distrito();
        distrito.setId(1L);

        alojamiento = new Alojamiento();
        alojamiento.setId(1L);
        alojamiento.setTitulo(alojamientoRequestDTO.getTitulo());
        alojamiento.setDistrito(distrito);
        alojamiento.setPropietario(propietario);

        alojamientoResponseDTO = new AlojamientoResponseDTO();
        alojamientoResponseDTO.setId(1L);
        alojamientoResponseDTO.setTitulo("Título de prueba");
    }

    @Test
    @DisplayName("Crear Alojamiento - Publicación Exitosa")
    void testCrearAlojamiento_PublicacionExitosa() throws IOException {
        // Arrange
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(true);
        when(alojamientoRepository.save(any(Alojamiento.class))).thenReturn(alojamiento);

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://example.com/image.jpg");
        uploadResult.put("public_id", "public_id_123");
        when(cloudinaryService.subirImagen(any(MultipartFile.class))).thenReturn(uploadResult);

        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(alojamientoResponseDTO);

        // Act
        AlojamientoResponseDTO result = alojamientoService.crearAlojamiento(alojamientoRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(alojamientoResponseDTO.getId(), result.getId());
        assertEquals(alojamientoResponseDTO.getTitulo(), result.getTitulo());

        verify(alojamientoRepository, times(1)).existsByNroPartida("12345678");
        verify(propietariosRepository, times(1)).findById(1L);
        verify(datosPropiedadesRepository, times(1)).existsByDniPropietarioAndNroPartida("12345678A", "12345678");
        verify(alojamientoRepository, times(1)).save(any(Alojamiento.class));
        verify(cloudinaryService, times(1)).subirImagen(any(MultipartFile.class));
        verify(imagenesRepository, times(1)).save(any());
        verify(mapper, times(1)).convertToDTO(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Descripción Corta")
    void testCrearAlojamiento_DescripcionCorta() {
        // Arrange
        alojamientoRequestDTO.setDescripcion("Depa"); // Menos de 50 caracteres

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("La descripción debe tener al menos 50 caracteres.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Precio Fuera de Rango (Menor)")
    void testCrearAlojamiento_PrecioMenorAlMinimo() {
        // Arrange
        alojamientoRequestDTO.setPrecioMensual(new BigDecimal("100.00")); // Menor a 200

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("El precio debe estar entre S/200 y S/5000.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Precio Fuera de Rango (Mayor)")
    void testCrearAlojamiento_PrecioMayorAlMaximo() {
        // Arrange
        alojamientoRequestDTO.setPrecioMensual(new BigDecimal("6000.00")); // Mayor a 5000

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("El precio debe estar entre S/200 y S/5000.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Nro Partida Duplicado")
    void testCrearAlojamiento_NroPartidaDuplicado() {
        // Arrange
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Distrito No Encontrado")
    void testCrearAlojamiento_DistritoNoEncontrado() {
        // Arrange
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Propietario No Encontrado")
    void testCrearAlojamiento_PropietarioNoEncontrado() {
        // Arrange
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Documentación Insuficiente")
    void testCrearAlojamiento_DocumentacionInsuficiente() {
        // Arrange
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        // Simular que la documentación (DNI + Nro Partida) no se encuentra
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("Datos no encontrados en base de datos", exception.getMessage());

        // Verificar que no se guardó el alojamiento ni se subieron imágenes
        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
        try {
            verify(cloudinaryService, never()).subirImagen(any());
        } catch (IOException e) {
            // ignore
        }
    }

    @Test
    @DisplayName("Crear Alojamiento - Sin Fotos")
    void testCrearAlojamiento_SinFotos() {
        // Arrange
        alojamientoRequestDTO.setImagenes(null); // No se sube ninguna imagen

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("Debe subir al menos una imagen para el alojamiento.", exception.getMessage());

        // Verificar que no se guardó el alojamiento
        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Límite de Ofertas Excedido")
    void testCrearAlojamiento_LimiteDeOfertasExcedido() {
        // Arrange
        // Simular que las validaciones previas pasan
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));

        // Simular que el propietario ya tiene 20 ofertas activas
        when(alojamientoRepository.countByPropietarioIdAndAlquiladoIsFalse(anyLong())).thenReturn(20L);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("Ha alcanzado el límite máximo de 20 ofertas activas.", exception.getMessage());

        // Verificar que no se intentó guardar el alojamiento
        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }


    @Test
    @DisplayName("Carga exitosa del mapa con ofertas")
    void testGetAllAlojamientos_CargaExitosaParaMapa() {
        // Arrange
        Alojamiento alojamiento1 = new Alojamiento();
        alojamiento1.setId(1L);
        alojamiento1.setTitulo("Alojamiento 1");

        Alojamiento alojamiento2 = new Alojamiento();
        alojamiento2.setId(2L);
        alojamiento2.setTitulo("Alojamiento 2");

        java.util.List<Alojamiento> listaAlojamientos = java.util.Arrays.asList(alojamiento1, alojamiento2);

        AlojamientoResponseDTO dto1 = new AlojamientoResponseDTO();
        dto1.setId(1L);
        dto1.setTitulo("Alojamiento 1");

        AlojamientoResponseDTO dto2 = new AlojamientoResponseDTO();
        dto2.setId(2L);
        dto2.setTitulo("Alojamiento 2");

        java.util.List<AlojamientoResponseDTO> listaDtos = java.util.Arrays.asList(dto1, dto2);

        when(alojamientoRepository.findAll()).thenReturn(listaAlojamientos);
        when(mapper.convertToListDTO(listaAlojamientos)).thenReturn(listaDtos);

        // Act
        java.util.List<AlojamientoResponseDTO> resultado = alojamientoService.getAllAlojamientos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaDtos, resultado);

        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(listaAlojamientos);
    }

    @Test
    @DisplayName("Navegación a zona sin ofertas")
    void testGetAllAlojamientos_NavegacionZonaSinOfertas() {
        // Arrange
        // Simula que el repositorio no encuentra alojamientos
        when(alojamientoRepository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.convertToListDTO(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        // Intenta obtener todos los alojamientos
        java.util.List<AlojamientoResponseDTO> resultado = alojamientoService.getAllAlojamientos();

        // Assert
        // El resultado debe ser una lista vacía, no nula
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        // Verifica que se llamó al repositorio
        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(Collections.emptyList());
    }


    @Test
    @DisplayName("Error de geolocalización")
    void testGetAllAlojamientos_ErrorGeolocalizacion() {
        // Arrange
        // Alojamiento con coordenadas
        Alojamiento alojamientoConCoord = new Alojamiento();
        alojamientoConCoord.setId(1L);
        alojamientoConCoord.setLatitud(-12.046374);
        alojamientoConCoord.setLongitud(-77.042793);

        // Alojamiento sin coordenadas (usará el valor por defecto 0.0 para double)
        Alojamiento alojamientoSinCoord = new Alojamiento();
        alojamientoSinCoord.setId(2L);

        java.util.List<Alojamiento> listaAlojamientos = java.util.Arrays.asList(alojamientoConCoord, alojamientoSinCoord);

        // DTO correspondiente con coordenadas
        AlojamientoResponseDTO dtoConCoord = new AlojamientoResponseDTO();
        dtoConCoord.setId(1L);
        dtoConCoord.setLatitud(-12.046374);
        dtoConCoord.setLongitud(-77.042793);

        // DTO correspondiente sin coordenadas (latitud y longitud son null)
        AlojamientoResponseDTO dtoSinCoord = new AlojamientoResponseDTO();
        dtoSinCoord.setId(2L);
        dtoSinCoord.setLatitud(null);
        dtoSinCoord.setLongitud(null);

        java.util.List<AlojamientoResponseDTO> listaDtos = java.util.Arrays.asList(dtoConCoord, dtoSinCoord);

        // Simular que el repositorio devuelve los alojamientos y el mapper los convierte
        when(alojamientoRepository.findAll()).thenReturn(listaAlojamientos);
        when(mapper.convertToListDTO(listaAlojamientos)).thenReturn(listaDtos);

        // Act
        java.util.List<AlojamientoResponseDTO> resultado = alojamientoService.getAllAlojamientos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        // Verifica que el primer DTO tiene coordenadas
        assertNotNull(resultado.get(0).getLatitud());
        assertNotNull(resultado.get(0).getLongitud());
        // Verifica que el segundo DTO tiene coordenadas nulas (incertidumbre)
        assertNull(resultado.get(1).getLatitud());
        assertNull(resultado.get(1).getLongitud());

        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(listaAlojamientos);
    }

    @DisplayName("Actualizar Alojamiento - No Encontrado")
    void testUpdateAlojamiento_NoEncontrado() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO));
    }

    @Test
    @DisplayName("Actualizar Alojamiento - Cambio de Distrito no permitido")
    void testUpdateAlojamiento_CambioDistrito() {
        // Arrange
        alojamiento.setDistrito(distrito);
        alojamientoRequestDTO.setDistritoId(2L); // ID de distrito diferente
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO));
        assertEquals("No se puede cambiar el distrito del alojamiento.", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar Alojamiento - Cambio de Propietario no permitido")
    void testUpdateAlojamiento_CambioPropietario() {
        // Arrange
        alojamiento.setPropietario(propietario);
        alojamientoRequestDTO.setPropietarioId(2L); // ID de propietario diferente
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO));
        assertEquals("No se puede cambiar el propietario del alojamiento.", exception.getMessage());
    }

    @Test
    @DisplayName("Agregar Imagenes - Alojamiento No Encontrado")
    void testAgregarImagenes_AlojamientoNoEncontrado() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());
        List<MultipartFile> nuevasImagenes = Collections.singletonList(mock(MultipartFile.class));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.agregarImagenes(1L, nuevasImagenes));
    }

    @Test
    @DisplayName("Agregar Imagenes - Lista de Imagenes Vacia")
    void testAgregarImagenes_ListaVacia() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> alojamientoService.agregarImagenes(1L, Collections.emptyList()));
    }

    @Test
    @DisplayName("Marcar como Alquilado - Alojamiento No Encontrado")
    void testMarcarComoAlquilado_NoEncontrado() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.marcarComoAlquilado(1L));
    }

    @Test
    @DisplayName("Obtener Vendedor - Alojamiento No Encontrado")
    void testObtenerDatosVendedor_AlojamientoNoEncontrado() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.obtenerDatosVendedor(1L));
    }

    @Test
    @DisplayName("Obtener Vendedor - Propietario No Asociado")
    void testObtenerDatosVendedor_PropietarioNoAsociado() {
        // Arrange
        alojamiento.setPropietario(null);
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.obtenerDatosVendedor(1L));
    }

    @Test
    @DisplayName("Eliminar Alojamiento - No Encontrado")
    void testDeleteAlojamiento_NoEncontrado() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.deleteAlojamiento(1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Alojamiento No Encontrado")
    void testEliminarImagen_AlojamientoNoEncontrado() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Alojamiento con una sola imagen")
    void testEliminarImagen_UnaSolaImagen() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(anyLong())).thenReturn(Collections.singletonList(new ImagenesAlojamiento()));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Imagen No Encontrada")
    void testEliminarImagen_ImagenNoEncontrada() {
        // Arrange
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(anyLong())).thenReturn(List.of(new ImagenesAlojamiento(), new ImagenesAlojamiento()));
        when(imagenesRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Imagen no pertenece al Alojamiento")
    void testEliminarImagen_NoPertenece() {
        // Arrange
        Alojamiento otroAlojamiento = new Alojamiento();
        otroAlojamiento.setId(2L);
        ImagenesAlojamiento imagen = new ImagenesAlojamiento();
        imagen.setId(1L);
        imagen.setAlojamiento(otroAlojamiento);

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(List.of(new ImagenesAlojamiento(), new ImagenesAlojamiento()));
        when(imagenesRepository.findById(1L)).thenReturn(Optional.of(imagen));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Obtener Alojamiento por ID - Encontrado")
    void testGetAlojamientoById_Encontrado() {
        // Arrange
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        // Act
        AlojamientoResponseDTO result = alojamientoService.getAlojamientoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(alojamientoResponseDTO.getId(), result.getId());
        verify(alojamientoRepository, times(1)).findById(1L);
        verify(mapper, times(1)).convertToDTO(alojamiento);
    }

    @Test
    @DisplayName("Obtener Alojamiento por ID - No Encontrado")
    void testGetAlojamientoById_NoEncontrado() {
        // Arrange
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.getAlojamientoById(1L));
        verify(alojamientoRepository, times(1)).findById(1L);
        verify(mapper, never()).convertToDTO(any());
    }

    @Test
    @DisplayName("Obtener todos los Alojamientos")
    void testGetAllAlojamientos() {
        // Arrange
        List<Alojamiento> alojamientos = Collections.singletonList(alojamiento);
        when(alojamientoRepository.findAll()).thenReturn(alojamientos);
        when(mapper.convertToListDTO(alojamientos)).thenReturn(Collections.singletonList(alojamientoResponseDTO));

        // Act
        List<AlojamientoResponseDTO> result = alojamientoService.getAllAlojamientos();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(alojamientos);
    }

    @Test
    @DisplayName("Listar Alojamientos por Distrito")
    void testListarPorDistrito() {
        // Arrange
        List<Alojamiento> alojamientos = Collections.singletonList(alojamiento);
        when(alojamientoRepository.findByDistritoId(1L)).thenReturn(alojamientos);
        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(alojamientoResponseDTO);

        // Act
        List<AlojamientoResponseDTO> result = alojamientoService.listarPorDistrito(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(alojamientoRepository, times(1)).findByDistritoId(1L);
        verify(mapper, times(1)).convertToDTO(alojamiento);
    }

    @Test
    @DisplayName("Listar Alojamientos por Universidad")
    void testListarPorUniversidad() {
        // Arrange
        List<Alojamiento> alojamientos = Collections.singletonList(alojamiento);
        when(alojamientoRepository.findByUniversidadId(1L)).thenReturn(alojamientos);
        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(alojamientoResponseDTO);

        // Act
        List<AlojamientoResponseDTO> result = alojamientoService.listarPorUniversidad(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(alojamientoRepository, times(1)).findByUniversidadId(1L);
        verify(mapper, times(1)).convertToDTO(alojamiento);
    }

    @Test
    @DisplayName("Actualizar Alojamiento - Éxito")
    void testUpdateAlojamiento_Exito() {
        // Arrange
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(alojamientoRepository.save(any(Alojamiento.class))).thenReturn(alojamiento);
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        alojamientoRequestDTO.setTitulo("Nuevo Título");

        // Act
        AlojamientoResponseDTO result = alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO);

        // Assert
        assertNotNull(result);
        verify(alojamientoRepository, times(1)).findById(1L);
        verify(alojamientoRepository, times(1)).save(alojamiento);
        assertEquals("Nuevo Título", alojamiento.getTitulo());
    }

    @Test
    @DisplayName("Eliminar Alojamiento - Éxito")
    void testDeleteAlojamiento_Exito() throws Exception {
        // Arrange
        ImagenesAlojamiento imagen = new ImagenesAlojamiento();
        imagen.setPublicId("public_id_test");
        List<ImagenesAlojamiento> imagenes = Collections.singletonList(imagen);

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(imagenes);
        when(transporteRepository.findByAlojamientoId(1L)).thenReturn(Collections.emptyList());
        when(uniAlojamientoRepository.findByAlojamientoId(1L)).thenReturn(Collections.emptyList());
        doNothing().when(cloudinaryService).eliminarImagen(anyString());

        // Act
        alojamientoService.deleteAlojamiento(1L);

        // Assert
        verify(alojamientoRepository, times(1)).findById(1L);
        verify(imagenesRepository, times(1)).findByAlojamientoId(1L);
        verify(cloudinaryService, times(1)).eliminarImagen("public_id_test");
        verify(imagenesRepository, times(1)).deleteAll(imagenes);
        verify(alojamientoRepository, times(1)).delete(alojamiento);
    }

    @Test
    @DisplayName("Crear Alojamiento - Éxito con Transportes y Universidades")
    void testCrearAlojamiento_ExitoCompleto() throws IOException {
        // Arrange
        alojamientoRequestDTO.setTransportes(List.of("Bus"));
        alojamientoRequestDTO.setUniversidadesIds(List.of(1L));

        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(true);
        when(alojamientoRepository.countByPropietarioIdAndAlquiladoIsFalse(anyLong())).thenReturn(0L);
        when(alojamientoRepository.save(any(Alojamiento.class))).thenReturn(alojamiento);
        when(universidadRepository.findById(1L)).thenReturn(Optional.of(new Universidad()));

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://example.com/image.jpg");
        uploadResult.put("public_id", "public_id_123");
        when(cloudinaryService.subirImagen(any(MultipartFile.class))).thenReturn(uploadResult);

        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(alojamientoResponseDTO);

        // Act
        AlojamientoResponseDTO result = alojamientoService.crearAlojamiento(alojamientoRequestDTO);

        // Assert
        assertNotNull(result);
        verify(transporteRepository, times(1)).saveAll(any());
        verify(uniAlojamientoRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Crear Alojamiento - Universidad No Encontrada")
    void testCrearAlojamiento_UniversidadNoEncontrada() throws IOException {
        // Arrange
        alojamientoRequestDTO.setUniversidadesIds(List.of(99L)); // ID no existente
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(true);
        when(alojamientoRepository.save(any(Alojamiento.class))).thenReturn(alojamiento);
        when(universidadRepository.findById(99L)).thenReturn(Optional.empty());

        // Configurar el mock de Cloudinary para evitar NullPointerException
        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://example.com/image.jpg");
        uploadResult.put("public_id", "public_id_123");
        when(cloudinaryService.subirImagen(any(MultipartFile.class))).thenReturn(uploadResult);


        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));
        verify(uniAlojamientoRepository, never()).saveAll(any());
    }


    @Test
    @DisplayName("Agregar Imagenes - Éxito")
    void testAgregarImagenes_Exito() throws IOException {
        // Arrange
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        List<MultipartFile> nuevasImagenes = List.of(mock(MultipartFile.class));
        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://example.com/new_image.jpg");
        uploadResult.put("public_id", "public_id_456");
        when(cloudinaryService.subirImagen(any(MultipartFile.class))).thenReturn(uploadResult);
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        // Act
        AlojamientoResponseDTO result = alojamientoService.agregarImagenes(1L, nuevasImagenes);

        // Assert
        assertNotNull(result);
        verify(imagenesRepository, times(1)).save(any(ImagenesAlojamiento.class));
    }

    @Test
    @DisplayName("Marcar como Alquilado - Éxito")
    void testMarcarComoAlquilado_Exito() {
        // Arrange
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(alojamientoRepository.save(alojamiento)).thenReturn(alojamiento);
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        // Act
        AlojamientoResponseDTO result = alojamientoService.marcarComoAlquilado(1L);

        // Assert
        assertNotNull(result);
        assertTrue(alojamiento.getAlquilado());
        verify(alojamientoRepository, times(1)).save(alojamiento);
    }

    @Test
    @DisplayName("Eliminar Alojamiento - Éxito con Transportes y Universidades")
    void testDeleteAlojamiento_ExitoConRelaciones() throws Exception {
        // Arrange
        List<ImagenesAlojamiento> imagenes = List.of(new ImagenesAlojamiento());
        List<Transporte> transportes = List.of(new Transporte());
        List<UniAlojamiento> relaciones = List.of(new UniAlojamiento());

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(imagenes);
        when(transporteRepository.findByAlojamientoId(1L)).thenReturn(transportes);
        when(uniAlojamientoRepository.findByAlojamientoId(1L)).thenReturn(relaciones);

        // Act
        alojamientoService.deleteAlojamiento(1L);

        // Assert
        verify(transporteRepository, times(1)).deleteAll(transportes);
        verify(uniAlojamientoRepository, times(1)).deleteAll(relaciones);
        verify(alojamientoRepository, times(1)).delete(alojamiento);
    }

    @Test
    @DisplayName("Eliminar Imagen - Éxito")
    void testEliminarImagen_Exito() throws IOException {
        // Arrange
        ImagenesAlojamiento imagenAEliminar = new ImagenesAlojamiento();
        imagenAEliminar.setId(1L);
        imagenAEliminar.setAlojamiento(alojamiento);
        imagenAEliminar.setPublicId("public_id_test");

        List<ImagenesAlojamiento> imagenes = List.of(imagenAEliminar, new ImagenesAlojamiento());

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(imagenes);
        when(imagenesRepository.findById(1L)).thenReturn(Optional.of(imagenAEliminar));
        doNothing().when(cloudinaryService).eliminarImagen("public_id_test");

        // Act
        alojamientoService.eliminarImagen(1L, 1L);

        // Assert
        verify(cloudinaryService, times(1)).eliminarImagen("public_id_test");
        verify(imagenesRepository, times(1)).delete(imagenAEliminar);
    }

}
