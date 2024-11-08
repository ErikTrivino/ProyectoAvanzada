package co.edu.uniquindio.proyecto.modelo.dto.cupon;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoCupon;
import co.edu.uniquindio.proyecto.modelo.enums.TipoCupon;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InformacionCuponDTO(

        String id,
        String nombre,
        float descuento,
        LocalDate fechaVencimiento,
        String codigo,
        EstadoCupon estado,
        TipoCupon tipo

) {
}
