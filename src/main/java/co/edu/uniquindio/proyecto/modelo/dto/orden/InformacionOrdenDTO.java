package co.edu.uniquindio.proyecto.modelo.dto.orden;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleOrden;

import java.time.LocalDateTime;
import java.util.List;

public record InformacionOrdenDTO(

        String id,
        String idCliente,

        LocalDateTime fechaCreacion,
        float total,

        List<DetalleOrden> items
) {
}
