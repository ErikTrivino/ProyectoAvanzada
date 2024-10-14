package co.edu.uniquindio.proyecto.servicios.interfaces;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import org.springframework.stereotype.Service;

@Service
public interface CarritoServicio {



    String eliminarItem(String idCarrito, String idEvento) throws Exception;

    void agregarItem(String idCarrito, DetalleCarrito item) throws Exception;
   // void editarItem(String idCarrito , DetalleCarrito item) throws  Exception;

   Carrito traerCarrito(String idCarrito) throws Exception;
}