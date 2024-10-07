package co.edu.uniquindio.proyecto.modelo.dto.cuenta;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ItemCuentaDTO(
        @NotBlank String id,
        @NotBlank @Length(max = 50)String nombre,
        @NotBlank @Length(max = 10)String telefono,
        @NotBlank @Email String email
) {
}
