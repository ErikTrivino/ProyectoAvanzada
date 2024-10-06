/*package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class CarritoTest {


    private CarritoRepo carritoRepo;

    private Carrito carrito;
    private List<Carrito> carritos;
    private List<DetalleCarrito> listaCarrito;

    @BeforeEach
    public void setUp() {
        carrito = new Carrito("ID_CARRITO", "Nombre", 300.00);
        carritos = Arrays.asList(carrito);
        listaCarrito = Arrays.asList(new DetalleCarrito(null, 2, "Concierto de Música", 300.00));
    }

    @AfterEach
    public void tearDown() {
        carritoRepo.deleteAll();
    }

    // Test para registrar un carrito
    @Test
    public void registrarCarritoTest() {
        // Guardar el carrito en la base de datos
        Carrito carritoGuardado = carritoRepo.save(carrito);

        // Verificar que no sea null
        assertNotNull(carritoGuardado);
        assertEquals("CARRITO_TEST", carritoGuardado.getIdCarrito());
    }

    // Test para actualizar un carrito
    @Test
    public void actualizarCarritoTest() {
        // Guardar un nuevo carrito
        carritoRepo.save(carrito);

        // Modificar un detalle del carrito
        carrito.setItems(List.of(new DetalleCarrito(null, 2, "Evento actualizado", 150.00)));
        carritoRepo.save(carrito);

        // Verificar que la actualización se haya realizado
        Carrito carritoActualizado = carritoRepo.findById(carrito.getIdCarrito()).orElseThrow();
        assertEquals(2, carritoActualizado.getItems().get(0).getCantidad());
    }

    // Test para listar carritos
    @Test
    public void listarCarritosTest() {
        List<Carrito> carritos = carritoRepo.findAll();

        // Verificar que haya al menos un carrito en la base de datos
        assertFalse(carritos.isEmpty());
    }

    // Test para eliminar un carrito
    @Test
    public void eliminarCarritoTest() {
        carritoRepo.save(carrito);

        carritoRepo.deleteById(carrito.getIdCarrito());

        Carrito carritoEliminado = carritoRepo.findById(carrito.getIdCarrito()).orElse(null);
        assertNull(carritoEliminado);
    }


}*/