package co.edu.uniquindio.proyecto.modelo.dto.evento;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record EditarEventoDTO(

         @NotBlank String imagenPortada,
         @NotBlank EstadoEvento estado,
         @NotBlank String descripcion,
         @NotBlank String imagenLocalidades,
         @NotBlank LocalDateTime fechaEvento,
         @NotBlank String id

) {
}
