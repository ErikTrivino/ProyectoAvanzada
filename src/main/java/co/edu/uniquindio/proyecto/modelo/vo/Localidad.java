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


    public Localidad(double precio, String nombre, int capacidadMaxima) {
        this.precio = precio;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
    }
}
