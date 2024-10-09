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
public class DetalleCarrito {

    private ObjectId idEvento;
    private int cantidad;
    private String nombreLocalidad;




}

