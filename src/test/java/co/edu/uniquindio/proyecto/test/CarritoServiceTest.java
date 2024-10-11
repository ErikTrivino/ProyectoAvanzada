package co.edu.uniquindio.proyecto.test;


import co.edu.uniquindio.proyecto.ProyectoApplication;
import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;

@SpringBootTest
@ContextConfiguration(classes = ProyectoApplication.class)
public class CarritoServiceTest {

    @Autowired
    private CarritoServicio carritoServicio;






    @Test
    void testAgregarItem() throws Exception {
        // Crear un nuevo DetalleCarrito
        DetalleCarrito item = new DetalleCarrito();
        item.setIdEvento("evento001");
        item.setCantidad(2);
        item.setNombreLocalidad("General");

        // Agregar el item al carrito
        carritoServicio.agregarItem("6701eedf2c4a9b1234567890", item);

        // Traer el carrito actualizado
        Carrito carritoActualizado = carritoServicio.traerCarrito("6701eedf2c4a9b1234567890");

        // Verificar que el item fue agregado
        assertEquals(1, carritoActualizado.getItems().size());
        assertEquals("evento001", carritoActualizado.getItems().get(0).getIdEvento());
        assertEquals(2, carritoActualizado.getItems().get(0).getCantidad());
    }

    @Test
    void testEliminarItem() throws Exception {
        // Crear un nuevo DetalleCarrito y agregarlo
        DetalleCarrito item = new DetalleCarrito();
        item.setIdEvento("evento002");
        item.setCantidad(1);
        item.setNombreLocalidad("VIP");
        carritoServicio.agregarItem("6701eefb2c4a9b1234567891", item);//carrito 2

        // Asegurarse que el item está en el carrito
       // assertEquals(1, carrito.getItems().size());

        // Eliminar el item
        String result = carritoServicio.eliminarItem("6701eefb2c4a9b1234567891", "evento002");

        // Verificar que el item fue eliminado
        assertEquals("Item eliminado correctamente", result);
        //assertEquals(0, carrito.getItems().size());
    }

    @Test
    void testTraerCarrito() throws Exception {
        // Crear un nuevo DetalleCarrito y agregarlo
        DetalleCarrito item = new DetalleCarrito();
        item.setIdEvento("evento003");
        item.setCantidad(3);
        item.setNombreLocalidad("Platea");
       // carritoServicio.agregarItem("6701eefb2c4a9b1234567892", item);///carrito 3

        // Traer el carrito
        Carrito carritoTraido = carritoServicio.traerCarrito("6701ed1fa81f609e1a5692fb");

        // Verificar que el carrito tiene los items correctos
        assertNotNull(carritoTraido);
        assertEquals(2, carritoTraido.getItems().size());
        assertEquals("6701eea02f877bfc0e9397cf", carritoTraido.getItems().get(0).getIdEvento());
        //assertEquals(3, carritoTraido.getItems().get(0).getCantidad());
    }



}
