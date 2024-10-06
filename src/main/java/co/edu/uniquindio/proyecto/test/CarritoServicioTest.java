package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarritoServicioTest {


    private CarritoServicio carritoServicio;

    private Carrito carrito;

    @BeforeEach
    public void setUp() throws Exception {
        // Crear un carrito de ejemplo antes de cada test
        carrito = new Carrito();
        carrito.setFecha(LocalDateTime.now());
        carrito.setItems(List.of(new DetalleCarrito(null, 2, "VIP", 150.00f))); // Creación de un item inicial
        carrito.setIdCarrito("CARRITO_TEST");
        carritoServicio.agregarItem("CARRITO_TEST", new DetalleCarrito(null, 2, "VIP", 150.00f));
    }

    @Test
    public void testAgregarItem() throws Exception {
        // Agregar un nuevo item al carrito
        DetalleCarrito nuevoItem = new DetalleCarrito(null, 3, "General", 100.00f);
        carritoServicio.agregarItem("CARRITO_TEST", nuevoItem);

        // Verificar que el carrito tiene el nuevo item
        float total = carritoServicio.calcularTotal("CARRITO_TEST");
        assertEquals(450.00f, total, "El total no coincide con el esperado.");
    }

    @Test
    public void testEliminarItem() throws Exception {
        // Eliminar el item agregado en setUp()
        carritoServicio.eliminarItem("CARRITO_TEST", "ID_EVENTO"); // Reemplaza "ID_EVENTO" por un ID válido si lo tienes

        // Verificar que el total es 0 ya que solo había un item
        float total = carritoServicio.calcularTotal("CARRITO_TEST");
        assertEquals(0f, total, "El total debería ser 0 después de eliminar el item.");
    }

    @Test
    public void testCalcularTotal() throws Exception {
        // Verificar el total calculado correctamente
        float total = carritoServicio.calcularTotal("CARRITO_TEST");
        assertEquals(300.00f, total, "El total del carrito es incorrecto.");
    }
}

