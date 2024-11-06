package co.edu.uniquindio.proyecto.modelo.dto.evento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ItemEventoDTO(
        String urlImagenPoster,
        String nombre,
        LocalDate fecha,
        String ciudad
) {
}
