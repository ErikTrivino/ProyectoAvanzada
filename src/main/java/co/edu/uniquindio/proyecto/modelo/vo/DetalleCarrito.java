package co.edu.uniquindio.proyecto.modelo.vo;

import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DetalleCarrito {

    private String idString;
    private String idEvento;
    private int cantidad;
    private String nombreLocalidad;


}

