package co.edu.uniquindio.proyecto.modelo.vo;

import lombok.*;
import org.bson.types.ObjectId;



@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCarrito {

    private String productoId; // Corresponde al campo productoId en MongoDB
    private int cantidad;
    private double precioUnitario;



}

