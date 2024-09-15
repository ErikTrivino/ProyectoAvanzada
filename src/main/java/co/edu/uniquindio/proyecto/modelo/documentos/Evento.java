package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    @Id
    @EqualsAndHashCode.Exclude
    private String id;

    private String imagenPortada;
    private EstadoEvento estado;
    private String nombre;
    private String descripcion;
    private String imagenLocalidades;
    private TipoEvento tipo;
    private LocalDateTime fechaEvento;
    private String ciudad;
    private List<Localidad> localidades;

    public Evento(String imagenPortada, String id, EstadoEvento estado, String nombre, String descripcion, String imagenLocalidades, TipoEvento tipo, LocalDateTime fechaEvento, String ciudad, List<Localidad> localidades) {
        this.imagenPortada = imagenPortada;
        this.id = id;
        this.estado = estado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenLocalidades = imagenLocalidades;
        this.tipo = tipo;
        this.fechaEvento = fechaEvento;
        this.ciudad = ciudad;
        this.localidades = localidades;
    }
}
