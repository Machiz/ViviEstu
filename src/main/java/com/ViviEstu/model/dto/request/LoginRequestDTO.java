package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO{
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe ser valido")
        String correo;

        @NotBlank(message = "La contrasenia es obligatoria")
        String contrasenia;

}
