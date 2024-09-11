package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleOrden;
import co.edu.uniquindio.proyecto.modelo.vo.Pago;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public class Orden {

    private ObjectId idCliente;
    private LocalDateTime fecha;
    private String codigoPasarela;
    private List<DetalleOrden> items;
    private Pago pago;
    private String id;
    private float total;
    private ObjectId idCupon;
}
