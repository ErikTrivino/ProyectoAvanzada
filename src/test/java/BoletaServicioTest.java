import co.edu.uniquindio.proyecto.ProyectoApplication;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.InformacionCuentaDTO;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoBoleta;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import java.time.LocalDateTime;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = ProyectoApplication.class)
public class BoletaServicioTest {

    @Autowired
    private CuentaServicio cuentaServicio;

    private static final String ID_PROPIETARIO = "6701ed1fa81f609e1a5692fb";

    @Test
    public void buscarBoletasPorPropietarioTest() throws Exception {
        // Simular la respuesta esperada
        List<Boleta> boletasSimuladas = List.of(
                new Boleta("6701eea02f877bfc0e93971231", "6701eea02f877bfc0e9397cf", ID_PROPIETARIO, "evento1", LocalDateTime.now(), "VIP", EstadoBoleta.ACEPTADA, "IdPropietarioOriginal"),
                new Boleta("6701eea02f877bfc0e9397cf23", "6701eea02f877bfc0e9397cf", ID_PROPIETARIO, "evento1", LocalDateTime.now(), "VIP", EstadoBoleta.ACEPTADA, "IdPropietarioOriginal")
                //new Boleta("boleta1", "evento1", "Concierto A", LocalDateTime.now(), "VIP", 2, ID_PROPIETARIO, EstadoBoleta.ACEPTADA, ID_PROPIETARIO),
                //new Boleta("boleta2", "evento2", "Concierto B", LocalDateTime.now(), "General", 1, ID_PROPIETARIO, EstadoBoleta.ACEPTADA , ID_PROPIETARIO)
        );

        // Simular el comportamiento de cuentaServicio para este caso


        // Llamar al servicio y verificar el resultado
        List<Boleta> boletas = cuentaServicio.buscarBoletasPorPropietario(ID_PROPIETARIO);

        // Verificar que se obtienen las boletas correctas
        assertEquals(3, boletas.size());
        assertEquals("670756852fdf2526017894e3", boletas.get(0).getId());
        assertEquals("6707568b3591fb84abdb4f9c", boletas.get(1).getId());
        assertEquals("67075691abfd13b4d89d541f", boletas.get(2).getId());
    }

    @Test
    public void obtenerDetalleBoletaTest() throws Exception {
        // Simular la respuesta esperada
        Boleta boletaSimulada = new Boleta(
//                "boleta1",
//                "evento1",
//                "Concierto A",
//                LocalDateTime.now(),
//                "VIP",
//                2,
//                "propietario001",
//                "aceptada",
//                "propietario001"
        );


        // Simular el comportamiento de cuentaServicio
        ///when(cuentaServicio.obtenerDetalleBoleta("boleta1")).thenReturn(boletaSimulada);

        // Llamar al servicio y verificar el resultado
        Boleta boleta = cuentaServicio.obtenerDetalleBoleta("670756852fdf2526017894e3", "6701ed1fa81f609e1a5692fb");

        // Verificar que los detalles sean correctos
        assertNotNull(boleta);
        assertEquals("670756852fdf2526017894e3", boleta.getId());
        assertEquals("Concierto Rock", boleta.getNombreEvento());
    }
    @Test
    public void listarBoletasEnviadasTest() throws Exception {
        // Simular la respuesta esperada
        List<Boleta> boletasEnviadasSimuladas = List.of(
                new Boleta("boleta3", "idevento", ID_PROPIETARIO, "evento1", LocalDateTime.now(), "VIP", EstadoBoleta.ACEPTADA, "IdPropietarioOriginal")

        );

        //cuentaServicio.("idusuario", boletasEnviadasSimuladas);
        // Simular el comportamiento de cuentaServicio
        //when(cuentaServicio.listarBoletasEnviadas(ID_PROPIETARIO)).thenReturn(boletasEnviadasSimuladas);

        // Llamar al servicio y verificar el resultado
        List<Boleta> boletasEnviadas = cuentaServicio.listarBoletasEnviadas(ID_PROPIETARIO);

        // Verificar que se devuelve la lista correcta
        assertEquals(2, boletasEnviadas.size());
        assertEquals("6707568b3591fb84abdb4f9c", boletasEnviadas.get(0).getId());
    }

    @Test
    public void listarBoletasPendientesTest() throws Exception {
        // Simular la respuesta esperada
        List<Boleta> boletasPendientesSimuladas = List.of(
                //new Boleta("boleta4", "evento4", "Teatro Y", LocalDateTime.now(), "VIP", 1, "propietario002", "pendiente", "propietario001")
                new Boleta("boleta4", "idevento", ID_PROPIETARIO, "evento1", LocalDateTime.now(), "VIP", EstadoBoleta.ACEPTADA, "IdPropietarioOriginal")

        );

        // Simular el comportamiento de cuentaServicio
        //when(cuentaServicio.listarBoletasPendientes(ID_PROPIETARIO)).thenReturn(boletasPendientesSimuladas);

        // Llamar al servicio y verificar el resultado
        List<Boleta> boletasPendientes = cuentaServicio.listarBoletasPendientes(ID_PROPIETARIO);

        // Verificar que se devuelve la lista correcta
        assertEquals(1, boletasPendientes.size());
        assertEquals("67075b6c729e4a6804365b3e", boletasPendientes.get(0).getId());
    }

    @Test
    public void transferirBoletaTest() throws Exception {

        // Llamar al método de transferir
        cuentaServicio.transferirBoleta("6707e621e96e75a8eb1cb0cd", "6701ed1fa81f609e1a5692fb", "67075cab0623a30f86f70d0d");
        InformacionCuentaDTO cuenta = cuentaServicio.obtenerInformacionCuenta("67075cab0623a30f86f70d0d");
        assertTrue(cuenta.boletas().stream().filter(x -> x.getId() == "6707e621e96e75a8eb1cb0cd").findFirst().isPresent());
        assertEquals(EstadoBoleta.PENDIENTE, cuenta.boletas().stream().filter(x-> x.getId() == "6707e621e96e75a8eb1cb0cd").findFirst().get().getEstado());

    }

    @Test
    public void aceptarBoletaTest() throws Exception {

        // Llamar al método de aceptar boleta
        cuentaServicio.aceptarBoleta("6707ec00a26d03698ccbc650", "67075cab0623a30f86f70d0d");

    }





}
