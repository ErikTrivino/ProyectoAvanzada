package co.edu.uniquindio.proyecto.modelo.dto.evento;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import lombok.Builder;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InformacionEventoDTO(

        String id,
        EstadoEvento estado,
        String nombre,
        String descripcion,
        TipoEvento tipo,
        LocalDate fechaEvento,
        String ciudad,
        String imagenPortada,
        String imagenLocalidades,
        List<Localidad> localidades
) {

}
