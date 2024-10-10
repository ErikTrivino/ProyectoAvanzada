package co.edu.uniquindio.proyecto.modelo.vo;


import co.edu.uniquindio.proyecto.modelo.enums.EstadoBoleta;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Boleta {
    private String id;
    private String idEvento;
    private String idClientepropietario;
    private String nombreEvento;
    private LocalDateTime fechaEvento;
    private String nombreLocalidad;
    private EstadoBoleta estado;
    private String IdPropietarioOriginal;

}
