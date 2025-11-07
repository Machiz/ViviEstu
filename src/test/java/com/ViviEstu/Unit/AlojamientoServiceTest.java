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

        AlojamientoResponseDTO result = alojamientoService.crearAlojamiento(alojamientoRequestDTO);

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
        alojamientoRequestDTO.setDescripcion("Depa");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("La descripción debe tener al menos 50 caracteres.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Precio Fuera de Rango (Menor)")
    void testCrearAlojamiento_PrecioMenorAlMinimo() {
        alojamientoRequestDTO.setPrecioMensual(new BigDecimal("100.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("El precio debe estar entre S/200 y S/5000.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Precio Fuera de Rango (Mayor)")
    void testCrearAlojamiento_PrecioMayorAlMaximo() {
        alojamientoRequestDTO.setPrecioMensual(new BigDecimal("6000.00"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("El precio debe estar entre S/200 y S/5000.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Nro Partida Duplicado")
    void testCrearAlojamiento_NroPartidaDuplicado() {
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Distrito No Encontrado")
    void testCrearAlojamiento_DistritoNoEncontrado() {
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Propietario No Encontrado")
    void testCrearAlojamiento_PropietarioNoEncontrado() {
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Documentación Insuficiente")
    void testCrearAlojamiento_DocumentacionInsuficiente() {
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("Datos no encontrados en base de datos", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
        try {
            verify(cloudinaryService, never()).subirImagen(any());
        } catch (IOException e) {

        }
    }

    @Test
    @DisplayName("Crear Alojamiento - Sin Fotos")
    void testCrearAlojamiento_SinFotos() {
        alojamientoRequestDTO.setImagenes(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("Debe subir al menos una imagen para el alojamiento.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Límite de Ofertas Excedido")
    void testCrearAlojamiento_LimiteDeOfertasExcedido() {
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));

        when(alojamientoRepository.countByPropietarioIdAndAlquiladoIsFalse(anyLong())).thenReturn(20L);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("Ha alcanzado el límite máximo de 20 ofertas activas.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }


    @Test
    @DisplayName("Carga exitosa del mapa con ofertas")
    void testGetAllAlojamientos_CargaExitosaParaMapa() {

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

        java.util.List<AlojamientoResponseDTO> resultado = alojamientoService.getAllAlojamientos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(listaDtos, resultado);

        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(listaAlojamientos);
    }

    @Test
    @DisplayName("Navegación a zona sin ofertas")
    void testGetAllAlojamientos_NavegacionZonaSinOfertas() {
        when(alojamientoRepository.findAll()).thenReturn(Collections.emptyList());
        when(mapper.convertToListDTO(Collections.emptyList())).thenReturn(Collections.emptyList());

        java.util.List<AlojamientoResponseDTO> resultado = alojamientoService.getAllAlojamientos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(Collections.emptyList());
    }


    @Test
    @DisplayName("Error de geolocalización")
    void testGetAllAlojamientos_ErrorGeolocalizacion() {

        Alojamiento alojamientoConCoord = new Alojamiento();
        alojamientoConCoord.setId(1L);
        alojamientoConCoord.setLatitud(-12.046374);
        alojamientoConCoord.setLongitud(-77.042793);

        Alojamiento alojamientoSinCoord = new Alojamiento();
        alojamientoSinCoord.setId(2L);

        java.util.List<Alojamiento> listaAlojamientos = java.util.Arrays.asList(alojamientoConCoord, alojamientoSinCoord);

        AlojamientoResponseDTO dtoConCoord = new AlojamientoResponseDTO();
        dtoConCoord.setId(1L);
        dtoConCoord.setLatitud(-12.046374);
        dtoConCoord.setLongitud(-77.042793);


        AlojamientoResponseDTO dtoSinCoord = new AlojamientoResponseDTO();
        dtoSinCoord.setId(2L);
        dtoSinCoord.setLatitud(null);
        dtoSinCoord.setLongitud(null);

        java.util.List<AlojamientoResponseDTO> listaDtos = java.util.Arrays.asList(dtoConCoord, dtoSinCoord);

        when(alojamientoRepository.findAll()).thenReturn(listaAlojamientos);
        when(mapper.convertToListDTO(listaAlojamientos)).thenReturn(listaDtos);

        java.util.List<AlojamientoResponseDTO> resultado = alojamientoService.getAllAlojamientos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertNotNull(resultado.get(0).getLatitud());
        assertNotNull(resultado.get(0).getLongitud());
        assertNull(resultado.get(1).getLatitud());
        assertNull(resultado.get(1).getLongitud());

        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(listaAlojamientos);
    }

    @DisplayName("Actualizar Alojamiento - No Encontrado")
    void testUpdateAlojamiento_NoEncontrado() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO));
    }

    @Test
    @DisplayName("Actualizar Alojamiento - Cambio de Distrito no permitido")
    void testUpdateAlojamiento_CambioDistrito() {
        alojamiento.setDistrito(distrito);
        alojamientoRequestDTO.setDistritoId(2L);
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO));
        assertEquals("No se puede cambiar el distrito del alojamiento.", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar Alojamiento - Cambio de Propietario no permitido")
    void testUpdateAlojamiento_CambioPropietario() {
        alojamiento.setPropietario(propietario);
        alojamientoRequestDTO.setPropietarioId(2L);
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO));
        assertEquals("No se puede cambiar el propietario del alojamiento.", exception.getMessage());
    }

    @Test
    @DisplayName("Agregar Imagenes - Alojamiento No Encontrado")
    void testAgregarImagenes_AlojamientoNoEncontrado() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());
        List<MultipartFile> nuevasImagenes = Collections.singletonList(mock(MultipartFile.class));

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.agregarImagenes(1L, nuevasImagenes));
    }

    @Test
    @DisplayName("Agregar Imagenes - Lista de Imagenes Vacia")
    void testAgregarImagenes_ListaVacia() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        assertThrows(IllegalArgumentException.class, () -> alojamientoService.agregarImagenes(1L, Collections.emptyList()));
    }

    @Test
    @DisplayName("Marcar como Alquilado - Alojamiento No Encontrado")
    void testMarcarComoAlquilado_NoEncontrado() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.marcarComoAlquilado(1L));
    }

    @Test
    @DisplayName("Obtener Vendedor - Alojamiento No Encontrado")
    void testObtenerDatosVendedor_AlojamientoNoEncontrado() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.obtenerDatosVendedor(1L));
    }

    @Test
    @DisplayName("Obtener Vendedor - Propietario No Asociado")
    void testObtenerDatosVendedor_PropietarioNoAsociado() {
        alojamiento.setPropietario(null);
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.obtenerDatosVendedor(1L));
    }

    @Test
    @DisplayName("Eliminar Alojamiento - No Encontrado")
    void testDeleteAlojamiento_NoEncontrado() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.deleteAlojamiento(1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Alojamiento No Encontrado")
    void testEliminarImagen_AlojamientoNoEncontrado() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Alojamiento con una sola imagen")
    void testEliminarImagen_UnaSolaImagen() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(anyLong())).thenReturn(Collections.singletonList(new ImagenesAlojamiento()));

        assertThrows(IllegalStateException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Imagen No Encontrada")
    void testEliminarImagen_ImagenNoEncontrada() {
        when(alojamientoRepository.findById(anyLong())).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(anyLong())).thenReturn(List.of(new ImagenesAlojamiento(), new ImagenesAlojamiento()));
        when(imagenesRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Eliminar Imagen - Imagen no pertenece al Alojamiento")
    void testEliminarImagen_NoPertenece() {
        Alojamiento otroAlojamiento = new Alojamiento();
        otroAlojamiento.setId(2L);
        ImagenesAlojamiento imagen = new ImagenesAlojamiento();
        imagen.setId(1L);
        imagen.setAlojamiento(otroAlojamiento);

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(List.of(new ImagenesAlojamiento(), new ImagenesAlojamiento()));
        when(imagenesRepository.findById(1L)).thenReturn(Optional.of(imagen));

        assertThrows(IllegalArgumentException.class, () -> alojamientoService.eliminarImagen(1L, 1L));
    }

    @Test
    @DisplayName("Obtener Alojamiento por ID - Encontrado/ US-08 Visualización detallada de oferta ")
    void testGetAlojamientoById_Encontrado() {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        AlojamientoResponseDTO result = alojamientoService.getAlojamientoById(1L);

        assertNotNull(result);
        assertEquals(alojamientoResponseDTO.getId(), result.getId());
        verify(alojamientoRepository, times(1)).findById(1L);
        verify(mapper, times(1)).convertToDTO(alojamiento);
    }

    @Test
    @DisplayName("Obtener Alojamiento por ID - No Encontrado/ US-08 Visualización detallada de oferta ")
    void testGetAlojamientoById_NoEncontrado() {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.getAlojamientoById(1L));
        verify(alojamientoRepository, times(1)).findById(1L);
        verify(mapper, never()).convertToDTO(any());
    }

    @Test
    @DisplayName("Obtener todos los Alojamientos")
    void testGetAllAlojamientos() {
        List<Alojamiento> alojamientos = Collections.singletonList(alojamiento);
        when(alojamientoRepository.findAll()).thenReturn(alojamientos);
        when(mapper.convertToListDTO(alojamientos)).thenReturn(Collections.singletonList(alojamientoResponseDTO));

        List<AlojamientoResponseDTO> result = alojamientoService.getAllAlojamientos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(alojamientoRepository, times(1)).findAll();
        verify(mapper, times(1)).convertToListDTO(alojamientos);
    }

    @Test
    @DisplayName("Listar Alojamientos por Distrito")
    void testListarPorDistrito() {
        List<Alojamiento> alojamientos = Collections.singletonList(alojamiento);
        when(alojamientoRepository.findByDistritoId(1L)).thenReturn(alojamientos);
        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(alojamientoResponseDTO);

        List<AlojamientoResponseDTO> result = alojamientoService.listarPorDistrito(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(alojamientoRepository, times(1)).findByDistritoId(1L);
        verify(mapper, times(1)).convertToDTO(alojamiento);
    }

    @Test
    @DisplayName("Listar Alojamientos por Universidad")
    void testListarPorUniversidad() {
        List<Alojamiento> alojamientos = Collections.singletonList(alojamiento);
        when(alojamientoRepository.findByUniversidadId(1L)).thenReturn(alojamientos);
        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(alojamientoResponseDTO);

        List<AlojamientoResponseDTO> result = alojamientoService.listarPorUniversidad(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(alojamientoRepository, times(1)).findByUniversidadId(1L);
        verify(mapper, times(1)).convertToDTO(alojamiento);
    }

    @Test
    @DisplayName("Actualizar Alojamiento - Éxito")
    void testUpdateAlojamiento_Exito() {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(alojamientoRepository.save(any(Alojamiento.class))).thenReturn(alojamiento);
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        alojamientoRequestDTO.setTitulo("Nuevo Título");

        AlojamientoResponseDTO result = alojamientoService.updateAlojamiento(1L, alojamientoRequestDTO);

        assertNotNull(result);
        verify(alojamientoRepository, times(1)).findById(1L);
        verify(alojamientoRepository, times(1)).save(alojamiento);
        assertEquals("Nuevo Título", alojamiento.getTitulo());
    }

    @Test
    @DisplayName("Eliminar Alojamiento - Éxito")
    void testDeleteAlojamiento_Exito() throws Exception {
        ImagenesAlojamiento imagen = new ImagenesAlojamiento();
        imagen.setPublicId("public_id_test");
        List<ImagenesAlojamiento> imagenes = Collections.singletonList(imagen);

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(imagenes);
        when(transporteRepository.findByAlojamientoId(1L)).thenReturn(Collections.emptyList());
        when(uniAlojamientoRepository.findByAlojamientoId(1L)).thenReturn(Collections.emptyList());
        doNothing().when(cloudinaryService).eliminarImagen(anyString());

        alojamientoService.deleteAlojamiento(1L);

        verify(alojamientoRepository, times(1)).findById(1L);
        verify(imagenesRepository, times(1)).findByAlojamientoId(1L);
        verify(cloudinaryService, times(1)).eliminarImagen("public_id_test");
        verify(imagenesRepository, times(1)).deleteAll(imagenes);
        verify(alojamientoRepository, times(1)).delete(alojamiento);
    }

    @Test
    @DisplayName("Crear Alojamiento - Éxito con Transportes y Universidades")
    void testCrearAlojamiento_ExitoCompleto() throws IOException {
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

        AlojamientoResponseDTO result = alojamientoService.crearAlojamiento(alojamientoRequestDTO);

        assertNotNull(result);
        verify(transporteRepository, times(1)).saveAll(any());
        verify(uniAlojamientoRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Crear Alojamiento - Universidad No Encontrada")
    void testCrearAlojamiento_UniversidadNoEncontrada() throws IOException {
        alojamientoRequestDTO.setUniversidadesIds(List.of(99L));
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(true);
        when(alojamientoRepository.save(any(Alojamiento.class))).thenReturn(alojamiento);
        when(universidadRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://example.com/image.jpg");
        uploadResult.put("public_id", "public_id_123");
        when(cloudinaryService.subirImagen(any(MultipartFile.class))).thenReturn(uploadResult);


        assertThrows(ResourceNotFoundException.class, () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));
        verify(uniAlojamientoRepository, never()).saveAll(any());
    }


    @Test
    @DisplayName("Agregar Imagenes - Éxito")
    void testAgregarImagenes_Exito() throws IOException {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        List<MultipartFile> nuevasImagenes = List.of(mock(MultipartFile.class));
        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://example.com/new_image.jpg");
        uploadResult.put("public_id", "public_id_456");
        when(cloudinaryService.subirImagen(any(MultipartFile.class))).thenReturn(uploadResult);
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        AlojamientoResponseDTO result = alojamientoService.agregarImagenes(1L, nuevasImagenes);

        assertNotNull(result);
        verify(imagenesRepository, times(1)).save(any(ImagenesAlojamiento.class));
    }

    @Test
    @DisplayName("Marcar como Alquilado - Éxito")
    void testMarcarComoAlquilado_Exito() {
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(alojamientoRepository.save(alojamiento)).thenReturn(alojamiento);
        when(mapper.convertToDTO(alojamiento)).thenReturn(alojamientoResponseDTO);

        AlojamientoResponseDTO result = alojamientoService.marcarComoAlquilado(1L);

        assertNotNull(result);
        assertTrue(alojamiento.getAlquilado());
        verify(alojamientoRepository, times(1)).save(alojamiento);
    }

    @Test
    @DisplayName("Eliminar Alojamiento - Éxito con Transportes y Universidades")
    void testDeleteAlojamiento_ExitoConRelaciones() throws Exception {
        List<ImagenesAlojamiento> imagenes = List.of(new ImagenesAlojamiento());
        List<Transporte> transportes = List.of(new Transporte());
        List<UniAlojamiento> relaciones = List.of(new UniAlojamiento());

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(imagenes);
        when(transporteRepository.findByAlojamientoId(1L)).thenReturn(transportes);
        when(uniAlojamientoRepository.findByAlojamientoId(1L)).thenReturn(relaciones);

        alojamientoService.deleteAlojamiento(1L);

        verify(transporteRepository, times(1)).deleteAll(transportes);
        verify(uniAlojamientoRepository, times(1)).deleteAll(relaciones);
        verify(alojamientoRepository, times(1)).delete(alojamiento);
    }

    @Test
    @DisplayName("Eliminar Imagen - Éxito")
    void testEliminarImagen_Exito() throws IOException {
        ImagenesAlojamiento imagenAEliminar = new ImagenesAlojamiento();
        imagenAEliminar.setId(1L);
        imagenAEliminar.setAlojamiento(alojamiento);
        imagenAEliminar.setPublicId("public_id_test");

        List<ImagenesAlojamiento> imagenes = List.of(imagenAEliminar, new ImagenesAlojamiento());

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(imagenes);
        when(imagenesRepository.findById(1L)).thenReturn(Optional.of(imagenAEliminar));
        doNothing().when(cloudinaryService).eliminarImagen("public_id_test");

        alojamientoService.eliminarImagen(1L, 1L);

        verify(cloudinaryService, times(1)).eliminarImagen("public_id_test");
        verify(imagenesRepository, times(1)).delete(imagenAEliminar);
    }


    @Test
    @DisplayName("Listar Alojamientos por Distrito - Sin Resultados")
    void testListarPorDistrito_SinResultados() {
        when(alojamientoRepository.findByDistritoId(1L)).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> alojamientoService.listarPorDistrito(1L)
        );

        assertEquals("No se encontraron alojamientos en este distrito. Intenta con otro cercano.",
                exception.getMessage());
        verify(alojamientoRepository, times(1)).findByDistritoId(1L);
    }

    @Test
    @DisplayName("Listar Alojamientos por Universidad - Sin Resultados")
    void testListarPorUniversidad_SinResultados() {
        when(alojamientoRepository.findByUniversidadId(1L)).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> alojamientoService.listarPorUniversidad(1L)
        );

        assertEquals("No se encontraron alojamientos cercanos a esta universidad.",
                exception.getMessage());
        verify(alojamientoRepository, times(1)).findByUniversidadId(1L);
    }


    @Test
    @DisplayName("Obtener Datos del Vendedor - Éxito")
    void testObtenerDatosVendedor_Exito() {
        Propietarios propietario = new Propietarios();
        propietario.setId(10L);
        propietario.setNombre("Luis");
        propietario.setApellidos("Pérez");
        propietario.setTelefono("987654321");
        propietario.setDni("87654321");
        User user = new User();
        user.setCorreo("luis@example.com");
        propietario.setUser(user);

        alojamiento.setPropietario(propietario);
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));

        var result = alojamientoService.obtenerDatosVendedor(1L);

        assertNotNull(result);
        assertEquals("Luis", result.getNombre());
        assertEquals("luis@example.com", result.getCorreo());
        verify(alojamientoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Eliminar Alojamiento - Error al eliminar imagen de Cloudinary")
    void testDeleteAlojamiento_ErrorEnCloudinary() throws Exception {
        ImagenesAlojamiento img = new ImagenesAlojamiento();
        img.setPublicId("public_id_falla");
        List<ImagenesAlojamiento> imagenes = Collections.singletonList(img);

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(imagenesRepository.findByAlojamientoId(1L)).thenReturn(imagenes);
        when(transporteRepository.findByAlojamientoId(1L)).thenReturn(Collections.emptyList());
        when(uniAlojamientoRepository.findByAlojamientoId(1L)).thenReturn(Collections.emptyList());

        doThrow(new IOException("Error simulado")).when(cloudinaryService).eliminarImagen("public_id_falla");

        alojamientoService.deleteAlojamiento(1L);

        verify(imagenesRepository, times(1)).deleteAll(imagenes);
        verify(alojamientoRepository, times(1)).delete(alojamiento);
    }

    @Test
    @DisplayName("Crear Alojamiento - Lista de imágenes vacía")
    void testCrearAlojamiento_ListaImagenesVacia() {
        alojamientoRequestDTO.setImagenes(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("Debe subir al menos una imagen para el alojamiento.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Descripción nula")
    void testCrearAlojamiento_DescripcionNula() {
        alojamientoRequestDTO.setDescripcion(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> alojamientoService.crearAlojamiento(alojamientoRequestDTO));

        assertEquals("La descripción debe tener al menos 50 caracteres.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Comparar Alojamientos - Éxito")
    void testCompararAlojamientos_Exito() {
        Alojamiento a1 = new Alojamiento(); a1.setId(1L);
        Alojamiento a2 = new Alojamiento(); a2.setId(2L);

        when(alojamientoRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(a1, a2));
        when(mapper.convertToDTO(any(Alojamiento.class)))
                .thenReturn(new AlojamientoResponseDTO());

        List<AlojamientoResponseDTO> result = alojamientoService.compararAlojamientos(List.of(1L, 2L));

        assertEquals(2, result.size());
        verify(alojamientoRepository, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Comparar Alojamientos - Menos de dos seleccionados")
    void testCompararAlojamientos_MenosDeDos() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alojamientoService.compararAlojamientos(List.of(1L))
        );
        assertEquals("Debe seleccionar al menos dos alojamientos para comparar.", ex.getMessage());
    }

    @Test
    @DisplayName("Comparar Alojamientos - No encontrados")
    void testCompararAlojamientos_NoEncontrados() {
        when(alojamientoRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(new Alojamiento()));

        assertThrows(ResourceNotFoundException.class,
                () -> alojamientoService.compararAlojamientos(List.of(1L, 2L)));
    }

    @Test
    @DisplayName("Comparar Alojamientos - Lista nula")
    void testCompararAlojamientos_ListaNula() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> alojamientoService.compararAlojamientos(null)
        );
        assertEquals("Debe seleccionar al menos dos alojamientos para comparar.", ex.getMessage());
    }
    @Test
    @DisplayName("Alojamiento alquilado - exito")
    void testMarcarComoAlquilado_Exitosamente() {
        // given
        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setId(1L);
        alojamiento.setAlquilado(false);

        AlojamientoResponseDTO responseDTO = new AlojamientoResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setAlquilado(true);

        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(alojamientoRepository.save(any(Alojamiento.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(responseDTO);

        AlojamientoResponseDTO resultado = alojamientoService.marcarComoAlquilado(1L);

        assertNotNull(resultado);
        assertTrue(resultado.getAlquilado());
        verify(alojamientoRepository).save(alojamiento);
    }
    @Test
    @DisplayName("Alojamiento alquilado - marcado por error")
    void testReactivarAlojamiento_MarcadoPorError_Exitosamente() {
        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setId(2L);
        alojamiento.setAlquilado(true);

        AlojamientoResponseDTO responseDTO = new AlojamientoResponseDTO();
        responseDTO.setId(2L);
        responseDTO.setAlquilado(false);

        when(alojamientoRepository.findById(2L)).thenReturn(Optional.of(alojamiento));
        when(alojamientoRepository.save(any(Alojamiento.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(responseDTO);

        AlojamientoResponseDTO resultado = alojamientoService.marcarComoDisponible(2L);

        assertNotNull(resultado);
        assertFalse(resultado.getAlquilado());
        verify(alojamientoRepository).save(alojamiento);
    }

}
