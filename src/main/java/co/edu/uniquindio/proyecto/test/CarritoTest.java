package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;


@SpringJUnitConfig
public class CarritoTest {


    private CarritoRepo carritoRepo;

    private Carrito carrito;
    private List<Carrito> carritos;
    private List<DetalleCarrito> listaCarrito;

    @BeforeEach
    public void setUp() {
        carrito = new Carrito("ID_CARRITO", "Nombre", 300.00);
        carritos = Arrays.asList(carrito);
        listaCarrito = Arrays.asList(new DetalleCarrito(2, "Concierto de Música", 300.00));
    }

    @AfterEach
    public void tearDown() {
        carritoRepo.deleteAll();
    }

    @Test
    public void testObtenerCarrito() {
        // ...
    }

    @Test
    public void testAgregarItem() {
        // ...
    }

    @Test
    public void testEliminarItem() {
        // ...
    }

    @Test
    public void testObtenerTablaCarrito() {
        // ...
    }
}