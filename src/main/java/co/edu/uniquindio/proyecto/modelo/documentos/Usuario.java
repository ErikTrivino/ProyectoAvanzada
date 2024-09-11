package co.edu.uniquindio.proyecto.modelo.documentos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Usuario {

    private String id;
    private String telefono;
    private String direccion;
    private String cedula;
    private String nombre;

    @Builder
    public Usuario(String cedula, String nombre, String telefono, String direccion) {
    }
}
