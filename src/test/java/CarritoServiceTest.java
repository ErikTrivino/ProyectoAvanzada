



import co.edu.uniquindio.proyecto.ProyectoApplication;
import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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


    @Test
    public void testAgregarItemAlCarrito() throws Exception {
        // Arrange: Creamos el carrito y un item de prueba
        List<DetalleCarrito> items = new ArrayList<>();
        ObjectId idEvento = new ObjectId();
        DetalleCarrito detalle = DetalleCarrito.builder()
                .idEvento(idEvento)
                .cantidad(2)
                .nombreLocalidad("Platea")
                .build();

        // Act: Se agrega el item al carrito
        carritoServicio.agregarItem(items.toString(), detalle);

        // Assert: Se verifica que el item se haya agregado correctamente
        assertEquals(1, items.size());
        assertEquals(idEvento, items.get(0).getIdEvento());
        assertEquals(2, items.get(0).getCantidad());
        assertEquals("Platea", items.get(0).getNombreLocalidad());
    }

}
