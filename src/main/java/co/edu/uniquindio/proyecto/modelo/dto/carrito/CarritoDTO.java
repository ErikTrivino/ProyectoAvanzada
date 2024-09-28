package co.edu.uniquindio.proyecto.modelo.dto.carrito;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public record CarritoDTO(
         LocalDateTime fecha,
         List<DetalleCarrito>items,
         String id,
         ObjectId idUsuario
) {
}
