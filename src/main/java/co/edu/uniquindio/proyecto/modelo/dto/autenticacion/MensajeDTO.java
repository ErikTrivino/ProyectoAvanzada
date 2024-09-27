package co.edu.uniquindio.proyecto.modelo.dto.autenticacion;

public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}

