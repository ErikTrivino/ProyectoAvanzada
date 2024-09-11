package co.edu.uniquindio.proyecto.modelo.documentos;

import co.edu.uniquindio.proyecto.modelo.enums.EstadoCupon;
import co.edu.uniquindio.proyecto.modelo.enums.TipoCupon;

import java.time.LocalDateTime;

public class Cupon {

    private float descuento;
    private LocalDateTime fechaVencimiento;
    private String codigo;
    private EstadoCupon estado;
    private TipoCupon tipo;
    private String id;
    private String nombre;
}
