package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoCuenta;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("cuenta")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {


    @Id
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



    @Builder
    public Cuenta(String email, String password, Rol rol, LocalDateTime fechaRegistro, Usuario usuario, EstadoCuenta estado, CodigoValidacion codigoValidacionRegistro, CodigoValidacion codigoValidacionPassword) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.usuario = usuario;
        this.estado = estado;
        this.codigoValidacionRegistro = codigoValidacionRegistro;
        this.codigoValidacionPassword = codigoValidacionPassword;
    }

}
