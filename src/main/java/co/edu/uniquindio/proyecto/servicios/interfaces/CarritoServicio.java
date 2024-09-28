package co.edu.uniquindio.proyecto.servicios.interfaces;

import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;

import java.util.List;

public interface CarritoServicio {

    String tablaCarrito(List<DetalleCarrito> listaCarrito) throws Exception;

    String eliminarItem(String idCarrito) throws Exception;

    void agregarItem(String idCarrito, DetalleCarrito item) throws Exception;

    float calcularTotal(String idCarrito) throws Exception;
}