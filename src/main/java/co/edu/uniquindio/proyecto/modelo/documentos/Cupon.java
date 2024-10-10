package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoCupon;
import co.edu.uniquindio.proyecto.modelo.enums.TipoCupon;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("cupon")

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cupon {

    @Id
    @EqualsAndHashCode.Exclude
    private String id;

    private float descuento;
    private LocalDateTime fechaVencimiento;
    private String codigo;
    private EstadoCupon estado;
    private TipoCupon tipo;

    private String nombre;
}
