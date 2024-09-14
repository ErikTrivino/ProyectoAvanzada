package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    private String imagenPortada;
    private EstadoEvento estado;
    private String nombre;
    private String descripcion;
    private String imagenLocalidades;
    private TipoEvento tipo;
    private LocalDateTime fechaEvento;
    private String ciudad;
    private String id;
    private List<Localidad> localidades;
}
