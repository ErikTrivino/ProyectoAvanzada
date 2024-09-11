package co.edu.uniquindio.proyecto.modelo.dto.evento;

import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;

public record FiltroEventoDTO(
        String nombre,
        TipoEvento tipo,
        String ciudad
) {
}
