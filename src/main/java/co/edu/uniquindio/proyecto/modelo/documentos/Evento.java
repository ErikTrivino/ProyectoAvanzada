package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;

import java.time.LocalDateTime;
import java.util.List;

public class Evento {

    private String imagenImportada;
    private EstadoEvento estado;
    private String nombre;
    private String descripcion;
    private String imagenLocalidades;
    private TipoEvento tipo;
    private LocalDateTime fecha;
    private String ciudad;
    private String id;
    private List<Localidad> localidades;
}
