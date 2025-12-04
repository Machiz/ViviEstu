package com.ViviEstu.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlojamientoRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 50, message = "La descripción debe tener al menos 50 caracteres")
    private String descripcion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "El precio mensual es obligatorio")
    @DecimalMin(value = "200.00", message = "El precio debe ser de al menos S/200")
    @DecimalMax(value = "5000.00", message = "El precio no puede exceder los S/5000")
    private BigDecimal precioMensual;

    @NotBlank(message = "El numero de partida es obligatorio")
    private String nroPartida;

    @NotNull(message = "Debe indicar si está alquilado")
    private Boolean alquilado;

    @NotNull(message = "El ID del propietario es obligatorio")
    private Long propietarioId;

    @NotNull(message = "El ID de la zona es obligatorio")
    private Long distritoId;

    @NotNull(message = "Debe indicar metros cuadrados")
    private Integer metrosCuadrados;

    @NotNull(message = "Debe indicar cantidad banios")
    private Integer banios;

    @NotNull(message = "Debe indicar cantidad dormitorios")
    private Integer dormitorios;

    private List<MultipartFile> imagenes;

    private List<String> transportes;

    private List<Long> universidadesIds;


}
