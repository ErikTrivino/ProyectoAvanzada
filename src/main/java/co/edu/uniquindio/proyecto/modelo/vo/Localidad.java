package co.edu.uniquindio.proyecto.modelo.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Localidad {

    private float precio;
    private String nombre;
    private int entradasVendidas;
    private int capacidadMaxima;
}
