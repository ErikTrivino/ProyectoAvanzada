package co.edu.uniquindio.proyecto.modelo.vo;

import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DetalleOrden {

    private String id;
    private String idEvento;
    private float precio;
    private String nombreLocalidad;
    private int cantidad;


}
