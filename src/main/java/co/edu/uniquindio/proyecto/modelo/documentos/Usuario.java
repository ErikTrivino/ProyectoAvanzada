package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private String id;
    private String telefono;
    private String direccion;
    private String cedula;
    private String nombre;

    @Builder
    public Usuario(String cedula, String nombre, String telefono, String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }
}
