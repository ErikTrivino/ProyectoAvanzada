package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoCuenta;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {


    @Id
    @EqualsAndHashCode.Exclude
    private String id;
    private String email;
    private String password;
    private Rol rol;
    private LocalDateTime fechaRegistro;
    private Usuario usuario;
    EstadoCuenta estado;
    private CodigoValidacion codigoValidacionRegistro;
    private CodigoValidacion codigoValidacionPassword;

    private List<Boleta> boletas;
    private  List<TipoEvento> preferencias;




}
