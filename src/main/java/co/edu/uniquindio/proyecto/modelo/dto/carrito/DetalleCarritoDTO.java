package co.edu.uniquindio.proyecto.modelo.dto.carrito;

public record DetalleCarritoDTO(
        String idDetalleCarrito,
        String idEvento,
        int cantidad,
        String nombreLocalidad
) {
}
