package co.edu.uniquindio.proyecto.modelo.dto.evento;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record CrearEventoDTO(

         @NotBlank String imagenImportada,
         @NotBlank @Length (min = 5, max = 100) String nombre,
         @NotBlank @Length (max = 500) String descripcion,
         @NotBlank String imagenLocalidades,
         @NotNull TipoEvento tipo,
         @NotNull LocalDateTime fechaEvento,
         @NotBlank @Length (max = 20) String ciudad,
         @NotEmpty List<LocalidadDTO> localidades

) {
}
