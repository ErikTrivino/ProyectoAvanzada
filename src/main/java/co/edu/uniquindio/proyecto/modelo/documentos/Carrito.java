package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public class Carrito {

    private LocalDateTime fecha;
    private List<DetalleCarrito> items;
    private String id;
    private ObjectId idUsuario;



}
