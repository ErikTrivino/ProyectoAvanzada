package co.edu.uniquindio.proyecto.modelo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class DetalleOrden {

    private String idDetalleOrden;
    private String idEvento;
    private float precio;
    private String nombreLocalidad;
    private int cantidad;
}
