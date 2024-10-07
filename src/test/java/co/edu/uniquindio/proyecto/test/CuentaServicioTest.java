package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.*;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoCuenta;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import co.edu.uniquindio.proyecto.repositorios.CuentaRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class CuentaServicioTest {

    @Autowired
    private CuentaServicio cuentaServicio;

    @Test
    public void crearCuentaTest() throws Exception {

        CrearCuentaDTO crearCuentaDTO = new CrearCuentaDTO(
                "1098798321",
                "Rosario Meléndez",
                "3124532875",
                "Carrera 21 #13-64",
                "rosario27@email.com",
                "password"
        );



        // Se espera que no se lance ninguna excepción
        assertDoesNotThrow( () -> {
            // Se crea la cuenta y se imprime el id
            String id = cuentaServicio.crearCuenta(crearCuentaDTO);
            // Se espera que el id no sea nulo
            assertNotNull(id);
        } );

    }

    @Test
    public void editarCuentaTest(){


        //Se define el id de la cuenta del usuario a actualizar, este id está en el dataset.js
        String idCuenta = "66fb6f2296a08b156b316846";


        //Se crea un objeto de tipo EditarCuentaDTO
        EditarCuentaDTO editarCuentaDTO = new EditarCuentaDTO(
                idCuenta,
                "Raul Quintero",
                "3124532875",
                "Nueva dirección"
        );


        //Se espera que no se lance ninguna excepción
        assertDoesNotThrow(() -> {
            //Se actualiza la cuenta del usuario con el id definido
            cuentaServicio.editarCuenta(editarCuentaDTO);


            //Obtenemos el detalle de la cuenta del usuario con el id definido
            InformacionCuentaDTO detalle = cuentaServicio.obtenerInformacionCuenta(idCuenta);

        });
    }


    @Test
    public void eliminarCuentaTest(){

        //Se define el id de la cuenta del usuario a eliminar, este id está en el dataset.js
        String idCuenta = "6702b36ce4319b7094fdf3d3";

        //Se elimina la cuenta del usuario con el id definido
        assertDoesNotThrow(() -> cuentaServicio.eliminarCuenta(idCuenta) );

        //Al intentar obtener la cuenta del usuario con el id definido se debe lanzar una excepción
        assertThrows(Exception.class, () -> cuentaServicio.obtenerInformacionCuenta(idCuenta) );
    }

    @Test
    public void listarCuentasTest() throws Exception {
        //Se obtiene la lista de las cuentas de los usuarios
        List<ItemCuentaDTO> lista = cuentaServicio.listarCuentas();

        //Se verifica que la lista no sea nula y que tenga 3 elementos (o los que hayan)
        assertFalse(lista.isEmpty());
        assertEquals(8, lista.size());
    }

    @Test
    public void obtenerInformacionCuentaTest() {

        // Se define el id de la cuenta del usuario, este id está en el dataset.js
        String idCuenta = "6701ec61eb812956e91267d7";

        // Se invoca el método obtenerInformacionCuenta y se espera que no lance excepciones
        assertDoesNotThrow(() -> {
            InformacionCuentaDTO informacion = cuentaServicio.obtenerInformacionCuenta(idCuenta);

            // Se validan los atributos de la cuenta para asegurarse de que la información es correcta
            assertNotNull(informacion);
            //assertEquals("1097743245", informacion.cedula());
            //assertEquals("Raul Quintero", informacion.nombre());
            //assertEquals("3125642189", informacion.telefono());
            //assertEquals("Cerros del Viento", informacion.direccion());
            //assertEquals("raul32@email.com", informacion.correo());
        });

    }

    @Test
    public void enviarCodigoRecuperacionPasswordTest() throws Exception {

        // Se define el correo de la cuenta del usuario a recuperar, este correo está en el dataset.js
        String correo = "unieventosfae@gmail.com";

        // Se verifica que el método no lanza ninguna excepción
        assertDoesNotThrow(() -> cuentaServicio.enviarCodigoRecuperacionPassword(correo));

        // Se busca la cuenta del usuario para verificar que se haya actualizado el código de validación
        Cuenta cuenta = cuentaServicio.obtenerPorEmail(correo);

        // Se verifica que el código de validación fue generado correctamente
        assertNotNull(cuenta.getCodigoValidacionPassword().getCodigo());

        // Se verifica que la fecha de creación del código de validación no sea anterior a la fecha actual
        assertTrue(cuenta.getCodigoValidacionPassword().getFechaCreacion().isBefore(LocalDateTime.now()));

    }


    @Test
    public void cambiarPasswordTest() throws Exception {
        // Preparar el DTO con un código de verificación válido
        String email = "unieventosfae@gmail.com";
        String codigoVerificacion = "N89IJQ"; // Este debería ser el código previamente generado y guardado
        String nuevaPassword = "newPassword";

        CambiarPasswordDTO cambiarPasswordDTO = new CambiarPasswordDTO(email, codigoVerificacion, nuevaPassword);

        // Llamar al método para cambiar la contraseña
        String resultado = cuentaServicio.cambiarPassword(cambiarPasswordDTO);

        // Comprobar que el resultado sea el esperado
        assertEquals("Su contraseña ha sido cambiada.", resultado);

    }

    @Test
    public void iniciarSesionTest() throws Exception {
        // Preparar el entorno
        String email = "unieventosfae@gmail.com";
        String passwordOriginal = "newPassword";

        // Caso 1: Credenciales correctas
        LoginDTO loginDTOCorrecto = new LoginDTO(email, passwordOriginal);

        // Llamar al método para iniciar sesión
        TokenDTO tokenDTO = cuentaServicio.iniciarSesion(loginDTOCorrecto);

        // Verificar que el token no sea nulo
        assertNotNull(tokenDTO, "El token no debería ser nulo.");

        // Caso 2: Contraseña incorrecta
        LoginDTO loginDTOIncorrecto = new LoginDTO(email, "wrongPassword");

        // Verificar que se lance una excepción al intentar iniciar sesión con la contraseña incorrecta
        Exception exception = assertThrows(Exception.class, () -> {
            cuentaServicio.iniciarSesion(loginDTOIncorrecto);
        });

        // Comprobar el mensaje de la excepción
        assertEquals("La contraseña es incorrecta", exception.getMessage());
    }


    @Test
    public void activarCuentaTest() throws Exception {
        // Preparar el entorno
        String tokenValido = "ZB3BBM";
        String tokenInvalido = "invalidToken123";
        String correo = "unieventosfae@gmail.com";

        // Crear y guardar una cuenta con el token de validación
        LocalDateTime fechaCreacion = LocalDateTime.now();
        CodigoValidacion codigoValidacion = new CodigoValidacion(fechaCreacion, tokenValido);

        // Caso 1: Activar cuenta con token válido
        ActivarCuentaDTO activarCuentaDTOValido = new ActivarCuentaDTO(tokenValido,correo);

        String resultado = cuentaServicio.activarCuenta(activarCuentaDTOValido);

        // Verificar que el mensaje de activación sea el esperado
        assertEquals("Cuenta activada exitosamente.", resultado);

        // Verificar que el estado de la cuenta se haya cambiado a ACTIVO
        Cuenta cuentaActivada = cuentaServicio.obtenerPorEmail(correo);
        assertEquals(EstadoCuenta.ACTIVO, cuentaActivada.getEstado());

        // Caso 2: Intentar activar cuenta con token inválido
        //ActivarCuentaDTO activarCuentaDTOInvalido = new ActivarCuentaDTO(tokenInvalido,correo);

        // Verificar que se lance una excepción al usar un token inválido
        //Exception exception = assertThrows(Exception.class, () -> {
          //  cuentaServicio.activarCuenta(activarCuentaDTOInvalido);
        //});

        // Comprobar el mensaje de la excepción
        //assertEquals("El token de activación es inválido.", exception.getMessage());

    }

    @Test
    public void enviarCodigoActivacionCuentaTest() throws Exception {

        // Se define el correo de la cuenta del usuario a recuperar, este correo está en el dataset.js
        String correo = "unieventosfae@gmail.com";

        // Se verifica que el método no lanza ninguna excepción
        assertDoesNotThrow(() -> cuentaServicio.enviarCodigoActivacionCuenta(correo));

        // Se busca la cuenta del usuario para verificar que se haya actualizado el código de validación
        Cuenta cuenta = cuentaServicio.obtenerPorEmail(correo);

        // Se verifica que el código de validación fue generado correctamente
        assertNotNull(cuenta.getCodigoValidacionRegistro().getCodigo());

        // Se verifica que la fecha de creación del código de validación no sea anterior a la fecha actual
        assertTrue(cuenta.getCodigoValidacionRegistro().getFechaCreacion().isBefore(LocalDateTime.now()));

    }


}
