import co.edu.uniquindio.proyecto.ProyectoApplication;
import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = ProyectoApplication.class)
public class CarritoServiceTest {

    @Autowired
    private CarritoServicio carritoServicio;

    @Autowired
    private CarritoRepo carritoRepo;



    @Test
    public void testAgregarItemAlCarrito() throws Exception {
        // Crear un carrito de prueba y guardarlo
        Carrito carrito = new Carrito();
        carrito.setIdCarrito("ID_TEST");
        carrito.setItems(new ArrayList<>());
        carrito.setIdUsuario(new ObjectId("651f3a9d6f1b2b3c4f5d1b4c")); // ID de usuario de prueba
        carritoRepo.save(carrito); // Guardar en la base de datos de pruebas

        // Crear un detalle del carrito
        DetalleCarrito item = new DetalleCarrito();
        item.setProductoId("6701eea02f877bfc0e9397cf"); // ID de evento ficticio
        item.setCantidad(2);
        item.setPrecioUnitario(12000); // Agregar el precio si es necesario

        // Agregar el item al carrito
        carritoServicio.agregarItem("ID_TEST", item);

        // Recuperar el carrito actualizado y verificar que se haya agregado el item
        Carrito carritoActualizado = carritoRepo.findById("ID_TEST").orElseThrow(() -> new Exception("Carrito no encontrado"));
        assertEquals(1, carritoActualizado.getItems().size());
        assertEquals("6701eea02f877bfc0e9397cf", carritoActualizado.getItems().get(0).getProductoId());
        assertEquals(2, carritoActualizado.getItems().get(0).getCantidad());
        assertEquals(12000, carritoActualizado.getItems().get(0).getPrecioUnitario(), 0.01);
    }

    @Test
    public void testListarCarritos() {
        // Crear y guardar varios carritos de prueba
        Carrito carrito1 = new Carrito();
        carrito1.setIdCarrito("ID_TEST_1");
        carrito1.setItems(new ArrayList<>());
        carritoRepo.save(carrito1);

        Carrito carrito2 = new Carrito();
        carrito2.setIdCarrito("ID_TEST_2");
        carrito2.setItems(new ArrayList<>());
        carritoRepo.save(carrito2);

        Carrito carrito3 = new Carrito();
        carrito3.setIdCarrito("ID_TEST_3");
        carrito3.setItems(new ArrayList<>());
        carritoRepo.save(carrito3);

        // Llamar al método listarCarritos
        List<Carrito> carritos = carritoServicio.listarCarritos();

        // Verificar que la lista no esté vacía y que contenga los carritos esperados
        assertNotNull(carritos);
        assertTrue(carritos.size() >= 3); // Verifica que al menos los 3 carritos de prueba estén presentes
        assertTrue(carritos.stream().anyMatch(c -> c.getIdCarrito().equals("ID_TEST_1")));
        assertTrue(carritos.stream().anyMatch(c -> c.getIdCarrito().equals("ID_TEST_2")));
        assertTrue(carritos.stream().anyMatch(c -> c.getIdCarrito().equals("ID_TEST_3")));
    }





}
