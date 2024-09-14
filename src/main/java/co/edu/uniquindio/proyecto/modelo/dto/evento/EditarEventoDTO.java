package co.edu.uniquindio.proyecto.modelo.dto.evento;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record EditarEventoDTO(

         @NotBlank String imagenPortada,
         @NotBlank EstadoEvento estado,
         @NotBlank String nombre,
         @NotBlank String descripcion,
         @NotBlank String imagenLocalidades,
         @NotBlank TipoEvento tipo,
         @NotBlank LocalDateTime fechaEvento,
         @NotBlank String ciudad

) {
}
