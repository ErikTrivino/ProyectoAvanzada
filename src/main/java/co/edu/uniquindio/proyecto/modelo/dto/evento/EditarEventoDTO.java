package co.edu.uniquindio.proyecto.modelo.dto.evento;

import co.edu.uniquindio.proyecto.modelo.dto.imagenDTO;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EditarEventoDTO(
        @NotBlank String id,
        @NotBlank String nombre,

        @NotBlank String descripcion,
       // @NotNull EstadoEvento estado,
        String ciudad,
        @NotNull LocalDate fechaEvento,

        @NotNull imagenDTO imagenPortada,
        @NotNull imagenDTO imagenLocalidades


) {
}
