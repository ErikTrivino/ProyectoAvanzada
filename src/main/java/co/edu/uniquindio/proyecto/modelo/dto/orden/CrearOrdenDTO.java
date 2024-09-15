package co.edu.uniquindio.proyecto.modelo.dto.orden;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleOrden;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record CrearOrdenDTO(

        @NotBlank @Length(max = 50) String idCliente,
        @NotBlank @Length(max = 50) String idEvento,
        @NotNull LocalDateTime fechaCreacion,
        @Positive float total,

        @NotNull List<DetalleOrden> items


) {
}
