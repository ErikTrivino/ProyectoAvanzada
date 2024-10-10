package co.edu.uniquindio.proyecto.modelo.dto.cuenta;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EditarCuentaDTO(
        @NotBlank String id,
        @NotBlank @Length(max = 50) String nombre,
        @NotBlank @Length(max = 10) String telefono,
        @Length(max = 100) String direccion
) {
}
