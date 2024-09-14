package co.edu.uniquindio.proyecto.modelo.dto.cupon;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoCupon;
import co.edu.uniquindio.proyecto.modelo.enums.TipoCupon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record EditarCuponDTO(

        @NotBlank String id,
        @NotBlank @Length(max = 50) String nombre,
        @Positive float descuento,
        @NotNull LocalDateTime fechaVencimiento,
        @NotBlank @Length(max = 20) String codigo,
        @NotNull EstadoCupon estado,
        @NotNull TipoCupon tipo

) {
}
