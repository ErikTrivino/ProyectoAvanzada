package co.edu.uniquindio.proyecto.modelo.dto.evento;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record CrearEventoDTO(

         String imagenImportada,
         @NotBlank @Length (min = 5, max = 100) String nombre,
         @NotBlank @Length (max = 500) String descripcion,
         @NotBlank String imagenLocalidades,
         @NotBlank TipoEvento tipo,
         @NotBlank LocalDateTime fechaEvento,
         @NotBlank @Length (max = 20) String ciudad,
         @NotBlank List<Localidad> localidades

) {
}
