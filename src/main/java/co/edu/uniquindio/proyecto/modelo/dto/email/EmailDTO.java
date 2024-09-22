package co.edu.uniquindio.proyecto.modelo.dto.email;

public record EmailDTO(

        String asunto,
        String cuerpo,
        String destinatario
) {
}
