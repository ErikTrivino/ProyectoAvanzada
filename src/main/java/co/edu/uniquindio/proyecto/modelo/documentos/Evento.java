package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@Document("evento")

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

    public Localidad obtenerLocalidad(String nombreLocalidad) throws Exception {
        // Busca la localidad dentro de la lista de localidades
        return localidades.stream()
                .filter(localidad -> localidad.getNombre().equalsIgnoreCase(nombreLocalidad))
                .findFirst()
                .orElseThrow(() -> new Exception("No se encontró la localidad con el nombre: " + nombreLocalidad));
    }

}
