package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.ProyectoApplication;
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



        // Llamar al servicio
        List<TipoEvento> preferencias = cuentaServicio.obtenerPreferencias();

        // Verificar que el resultado no sea nulo
        assertNotNull(preferencias);

        // Verificar que se obtienen las preferencias correctas
        assertEquals(5, preferencias.size());
        assertEquals(TipoEvento.DEPORTE, preferencias.get(0));
        assertEquals(TipoEvento.CONCIERTO, preferencias.get(1));
        assertEquals(TipoEvento.CULTURAL, preferencias.get(2));
    }

    @Test
    public void agregarPreferenciasUsuarioTest() throws Exception {
        // Simular el ID del usuario y las preferencias que se agregarán
        String idUsuario = "670d2a7614d99c1d4d8c20fa";
        List<TipoEvento> preferenciasSimuladas = List.of(
                TipoEvento.DEPORTE,
                TipoEvento.BELLEZA
        );


        // Llamar al método de agregar preferencias
        cuentaServicio.agregarPreferenciasUsuario(idUsuario, preferenciasSimuladas);
        assertEquals(preferenciasSimuladas.get(0), cuentaServicio.obtenerPreferenciasUsuario("670d2a7614d99c1d4d8c20fa").get(0));


    }

    @Test
    void testObtenerPreferenciasUsuario() throws Exception {
        // Configurar el usuario y sus preferencias de prueba
        String idUsuario = "6701ed1fa81f609e1a5692fb";
        List<TipoEvento> preferencias = List.of(TipoEvento.CONCIERTO, TipoEvento.DEPORTE);


        // Llamar al método y verificar el resultado
        List<TipoEvento> resultado = cuentaServicio.obtenerPreferenciasUsuario(idUsuario);

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(preferencias.get(1), resultado.get(0));
    }


}
