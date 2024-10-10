import co.edu.uniquindio.proyecto.ProyectoApplication;
import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = ProyectoApplication.class)
public class PreferenciaTest {
    @Autowired
    private CuentaServicio cuentaServicio;
    @Test
    public void obtenerPreferenciasTest() throws Exception {
        // Simular las preferencias que esperas que el servicio devuelva
        List<TipoEvento> preferenciasSimuladas = List.of(
                 TipoEvento.DEPORTE,
                 TipoEvento.BELLEZA,
                 TipoEvento.MODA,
                 TipoEvento.CULTURAL,
                 TipoEvento.CONCIERTO

        );

        // Simular el comportamiento del servicio
        //when(cuentaServicio.obtenerPreferencias()).thenReturn(preferenciasSimuladas);

        // Llamar al servicio
        List<TipoEvento> preferencias = cuentaServicio.obtenerPreferencias();

        // Verificar que el resultado no sea nulo
        assertNotNull(preferencias);

        // Verificar que se obtienen las preferencias correctas
        assertEquals(3, preferencias.size());
        assertEquals("Concierto", preferencias.get(0));
        assertEquals("Deporte", preferencias.get(1));
        assertEquals("Teatro", preferencias.get(2));
    }

    @Test
    public void agregarPreferenciasUsuarioTest() throws Exception {
        // Simular el ID del usuario y las preferencias que se agregarán
        String idUsuario = "usuario001";
        List<TipoEvento> preferenciasSimuladas = List.of(
                TipoEvento.DEPORTE,
                TipoEvento.BELLEZA
        );

        // Simular el comportamiento del servicio. Como no devuelve nada, usamos doNothing
        //doNothing().when(cuentaServicio).agregarPreferenciasUsuario(idUsuario, preferenciasSimuladas);

        // Llamar al método de agregar preferencias
        cuentaServicio.agregarPreferenciasUsuario(idUsuario, preferenciasSimuladas);


        // Verificar que el método fue invocado con los parámetros correctos
        //verify(cuentaServicio, times(1)).agregarPreferenciasUsuario(idUsuario, preferenciasSimuladas);
    }

    @Test
    void testObtenerPreferenciasUsuario() throws Exception {
        // Configurar el usuario y sus preferencias de prueba
        String idUsuario = "usuario001";
        List<TipoEvento> preferencias = List.of(TipoEvento.CONCIERTO, TipoEvento.DEPORTE);

        Cuenta usuario = new Cuenta();

        // Simular que el usuario existe en el repositorio
        //when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(usuario));

        // Llamar al método y verificar el resultado
        List<TipoEvento> resultado = cuentaServicio.obtenerPreferenciasUsuario(idUsuario);

        assertNotNull(resultado);
        assertEquals(preferencias.size(), resultado.size());
        assertEquals(preferencias, resultado);
    }


}
