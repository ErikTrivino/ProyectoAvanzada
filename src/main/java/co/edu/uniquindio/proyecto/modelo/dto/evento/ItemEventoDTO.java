package co.edu.uniquindio.proyecto.modelo.dto.evento;

import java.time.LocalDateTime;

public record ItemEventoDTO(
        String id,
        String urlImagenPoster,
        String nombre,
        LocalDateTime fecha,
        String ciudad
) {
}
