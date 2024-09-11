package co.edu.uniquindio.proyecto.modelo.dto.cuenta;

public record CambiarPasswordDTO(

        String email,
        String codigoVerificacion,
        String passwordNueva
) {
}
