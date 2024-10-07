package co.edu.uniquindio.proyecto.modelo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class CodigoValidacion {

    private LocalDateTime fechaCreacion;
    private String codigo;


}
