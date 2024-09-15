package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleOrden;
import co.edu.uniquindio.proyecto.modelo.vo.Pago;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Orden {

    @Id
    @EqualsAndHashCode.Exclude
    private String id;

   // private ObjectId idCliente;
    private String idCliente;
    private LocalDateTime fecha;
    private String codigoPasarela;
    private List<DetalleOrden> items;
    private Pago pago;
    private float total;
    //private ObjectId idCupon;
    private String idCupon;
}
