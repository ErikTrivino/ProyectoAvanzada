import co.edu.uniquindio.proyecto.ProyectoApplication;
import co.edu.uniquindio.proyecto.modelo.documentos.Cupon;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoCupon;
import co.edu.uniquindio.proyecto.modelo.enums.TipoCupon;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuponServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = ProyectoApplication.class)
public class CuponServicioTest {
    @Autowired
    private CuponServicio cuponServicio;

    @Test
    public void crearCuponTest(){


        // Crear un DTO con los datos para crear una nueva cuenta
        CrearCuponDTO crearCuponDTO = new CrearCuponDTO(
                "Descuento del 20%",             // nombre
                20.0f,                            // descuento
                LocalDateTime.of(2024, 12, 31, 23, 59), // fechaVencimiento
                "CUPON2025",                      // codigo
                EstadoCupon.ACTIVO,               // estado
                TipoCupon.UNICO               // tipo
        );



        // Se espera que no se lance ninguna excepción
        assertDoesNotThrow( () -> {
            // Se crea la cuenta y se imprime el id
            Cupon cupon = cuponServicio. crearCupon(crearCuponDTO);
            // Se espera que el id no sea nulo
            assertEquals(crearCuponDTO.codigo(),cupon.getCodigo() );
        } );



    }
    @Test
    public void actualizarCuponTest() throws Exception {
        String idCupon = "652c95c6f0b56723d4638921";
        EditarCuponDTO editarCuponDTO = new EditarCuponDTO(
                idCupon,
                "Descuento del 25%",
                25.0f,
                LocalDateTime.of(2025, 1, 31, 23, 59),
                "CUPON2025",
                EstadoCupon.ACTIVO,
                TipoCupon.MULTIPLE
        );

        //InformacionCuponDTO cuponExistente = cuponServicio.obtenerInformacionCupon(idCupon);



        assertDoesNotThrow(() -> {
            Cupon cuponActualizado = cuponServicio.actualizarCupon(editarCuponDTO);
            assertNotNull(cuponActualizado);
            assertEquals(editarCuponDTO.codigo(), cuponActualizado.getCodigo());
            assertEquals(editarCuponDTO.descuento(), cuponActualizado.getDescuento());
            assertEquals(editarCuponDTO.estado(), cuponActualizado.getEstado());
            assertEquals(editarCuponDTO.tipo(), cuponActualizado.getTipo());
        });


    }
    @Test
    public void eliminarCuponTest() {
        String idCupon = "652c95c6f0b56723d4638924";

        assertDoesNotThrow(() -> {
           cuponServicio.eliminarCupon(idCupon);

           assertNull(cuponServicio.obtenerInformacionCupon(idCupon));// Validar mensaje de éxito
        });

    }

    @Test
    public void obtenerInformacionCuponTest() throws Exception {




        InformacionCuponDTO informacionCuponDTO = cuponServicio.obtenerInformacionCupon("652c95c6f0b56723d4638921");
        assertNotNull(informacionCuponDTO);
        assertEquals("CUPON2024", informacionCuponDTO.codigo());
        assertEquals(20.0f, informacionCuponDTO.descuento(), 0.001);
    }





    @Test
    public void listarCuponesActivosTest() {
        assertDoesNotThrow(() -> {
            List<Cupon> cuponesActivos = cuponServicio.listarCuponesActivos();
            //assertNotNull(cuponesActivos);
//            for (Cupon c:cuponesActivos
//                 ) {
//                System.out.println(c.getEstado());
//            }
            assertTrue(cuponesActivos.stream().allMatch(cupon -> cupon.getEstado() == EstadoCupon.ACTIVO));
        });
    }

    /**
     * Test para redimir un cupón.
     */
    @Test
    public void redimirCuponTest() {
        String codigoCupon = "CUPON2023";  // Este código debería existir en la base de datos para la prueba
        assertDoesNotThrow(() -> {
            boolean resultado = cuponServicio.redimirCupon(codigoCupon);
            assertTrue(resultado);  // Se espera que la redención sea exitosa
        });
    }



    /**
     * Test para listar cupones que están por expirar.
     */
    @Test
    public void listarCuponesPorExpirarTest() {
        assertDoesNotThrow(() -> {
            List<Cupon> cuponesPorExpirar = cuponServicio.listarCuponesPorExpirar();
            assertNotNull(cuponesPorExpirar);
            assertTrue(cuponesPorExpirar.stream()
                    .allMatch(cupon -> cupon.getFechaVencimiento().isBefore(LocalDateTime.now().plusDays(30))));
        });
    }

    @Test
    public void listarCuponesPorTipoTest() throws Exception {


        // Llamar al servicio para listar cupones por tipo
        List<Cupon> cupones = cuponServicio.listarCuponesPorTipo(TipoCupon.UNICO);

        // Verificar que el tamaño de la lista es el esperado
        assertEquals(3, cupones.size());

        // Verificar que todos los cupones devueltos son del tipo "UNICO"
        for (Cupon cupon : cupones) {
            assertEquals(TipoCupon.UNICO, cupon.getTipo());
        }

        // Verificar que uno de los cupones tiene el código correcto
        assertEquals("CUPON2024", cupones.get(0).getCodigo());
    }
}
