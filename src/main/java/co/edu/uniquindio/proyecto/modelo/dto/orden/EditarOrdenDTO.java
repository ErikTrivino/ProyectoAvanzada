package co.edu.uniquindio.proyecto.modelo.dto.orden;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleOrden;
import co.edu.uniquindio.proyecto.modelo.vo.Pago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record EditarOrdenDTO (


        @NotBlank String id,
        @NotBlank @Length(max = 50) String idCliente,

        @NotNull LocalDateTime fecha,
        @NotBlank String  codigoPasarela,

        @Positive float total,

        //@NotNull Pago pago,
       // @NotNull String idCupon,
        @NotNull List<DetalleOrden> items
){
}
