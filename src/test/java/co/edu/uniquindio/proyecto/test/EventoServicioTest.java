package co.edu.uniquindio.proyecto.test;

import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.dto.evento.*;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.servicios.interfaces.EventoServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventoServicioTest {

    @Autowired
    private EventoServicio eventoServicio;

    @Test
    public void crearEventoTest() throws Exception {

        // Se define un CrearEventoDTO con una fecha válida (posterior a la actual) y datos de evento únicos
        CrearEventoDTO crearEventoDTO = new CrearEventoDTO(
                "imagen.jpg",                                // Imagen del evento
                "Evento de Prueba",                          // Nombre del evento
                "Descripción del evento de prueba",          // Descripción del evento
                "imagen_localidades.jpg",                    // Imagen de las localidades
                TipoEvento.CONCIERTO,                      // Tipo del evento
                LocalDateTime.now().plusDays(30),             // Fecha del evento en el futuro
                "Medellín",                                  // Ciudad
                Arrays.asList(new LocalidadDTO("VIP", 250000.0,200),
                              new LocalidadDTO("General", 100000.0,530),
                              new LocalidadDTO("Palco", 2500000.0,20))      // Localidades
        );

        assertDoesNotThrow( () -> {
            // Se crea la cuenta y se imprime el id
            String resultado = eventoServicio.crearEvento(crearEventoDTO);
            // Se espera que el id no sea nulo
            assertNotNull(resultado);
        } );
    }

    @Test
    public void editarEventoTest() {

        // Se define el id del evento a actualizar, este id debe estar en el dataset.js
        String idEvento = "670d2abb52bd187deddbace8";

        // Crear el objeto EditarEventoDTO con los nuevos datos del evento
        EditarEventoDTO editarEventoDTO = new EditarEventoDTO(
                "nueva_imagen_portada.jpg",
                EstadoEvento.ACTIVO,
                "Descripción actualizada del evento",
                "Imagen actualizada para las localidades",
                LocalDateTime.of(2024, 12, 15, 20, 0),  // Fecha nueva
                idEvento //
        );

        // Verificar que no se lance ninguna excepción al editar el evento
        assertDoesNotThrow(() -> {
            // Se actualiza el evento con el id definido
            eventoServicio.editarEvento(editarEventoDTO);

        });
    }


    @Test
    public void eliminarEventoTest(){

        String idEvento = "6701eea02f877bfc0e9397cf";

        //Se elimina la cuenta del usuario con el id definido
        assertDoesNotThrow(() -> eventoServicio.eliminarEvento(idEvento) );

        //Al intentar obtener la cuenta del usuario con el id definido se debe lanzar una excepción
        assertThrows(Exception.class, () -> eventoServicio.obtenerEvento(idEvento));

    }

    @Test
    public void obtenerInformacionEventoTest(){
        // Se define el id de la cuenta del usuario, este id está en el dataset.js
        String idEvento = "670d2abb52bd187deddbace8";

        // Se invoca el método obtenerInformacionCuenta y se espera que no lance excepciones
        assertDoesNotThrow(() -> {
            InformacionEventoDTO informacion = eventoServicio.obtenerInformacionEvento(idEvento);

            assertNotNull(informacion);
        });

    }

    @Test
    public void listarEventosTest() throws Exception {

        List<ItemEventoDTO> lista = eventoServicio.listarEventos();

        //Se verifica que la lista no sea nula y que tenga 3 elementos (o los que hayan)
        assertEquals(7, lista.size());
        assertFalse(lista.isEmpty());
    }

    @Test
    public void filtrarEventosTest() {

        // Se crea un objeto de tipo FiltroEventoDTO con los criterios de búsqueda
        FiltroEventoDTO filtroEventoDTO = new FiltroEventoDTO(
                "Exposición de Arte", // Nombre
                TipoEvento.CONCIERTO, // Tipo
                "Cartagena" // Ciudad
        );

        // Se espera que no se lance ninguna excepción al filtrar los eventos
        assertDoesNotThrow(() -> {
            // Se filtran los eventos utilizando el servicio
            List<ItemEventoDTO> eventosFiltrados = eventoServicio.filtrarEventos(filtroEventoDTO);

            // Verificamos que la lista de eventos filtrados no esté vacía
            assertNotNull(eventosFiltrados);
            assertFalse(eventosFiltrados.isEmpty());

            // Verificamos que los eventos filtrados cumplan con los criterios de búsqueda
            for (ItemEventoDTO evento : eventosFiltrados) {
                assertEquals("Cartagena", evento.ciudad()); // Aquí deberías comparar con el filtro, que es "Cartagena"
                assertEquals(TipoEvento.CONCIERTO, filtroEventoDTO.tipo());
            }
        });
    }


}
