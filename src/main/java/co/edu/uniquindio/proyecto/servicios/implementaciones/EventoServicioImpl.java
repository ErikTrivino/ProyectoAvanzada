package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.dto.evento.*;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoEvento;
import co.edu.uniquindio.proyecto.repositorios.EventoRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.EventoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventoServicioImpl implements EventoServicio {

    private final EventoRepo eventoRepo;

    @Override
    public String crearEvento(CrearEventoDTO crearEventoDTO) throws Exception {

        if(crearEventoDTO.fechaEvento().isBefore(LocalDateTime.now())){
            throw new Exception("La fecha ingresada para el evento debe ser mayor a la fecha actual");
        }

        if( existeEvento(crearEventoDTO.fechaEvento(), crearEventoDTO.nombre(), crearEventoDTO.ciudad())){

            throw new Exception("Ya existe un evento registrado con el nombre " +
                    crearEventoDTO.nombre() + " para la fecha " + crearEventoDTO.fechaEvento());

        }

        Evento nuevoEvento = new Evento();
        nuevoEvento.setFechaEvento(crearEventoDTO.fechaEvento());
        nuevoEvento.setNombre(crearEventoDTO.nombre());
        nuevoEvento.setDescripcion(crearEventoDTO.descripcion());
        nuevoEvento.setEstado(EstadoEvento.ACTIVO);
        nuevoEvento.setTipo(crearEventoDTO.tipo());
        nuevoEvento.setCiudad(crearEventoDTO.ciudad());
        nuevoEvento.setLocalidades(crearEventoDTO.localidades()
        );

        eventoRepo.save(nuevoEvento);

        return "El evento ha sido creado con éxito.";
    }

    @Override
    public String editarEvento(EditarEventoDTO editarEventoDTO) throws Exception {

        
        return "Los cambios se han guardado correctamente.";
    }

    @Override
    public String eliminarEvento(String id) throws Exception {
        return "";
    }

    @Override
    public InformacionEventoDTO obtenerInformacionEvento(String id) throws Exception {
        return null;
    }

    @Override
    public List<ItemEventoDTO> listarEventos() throws Exception {
        return List.of();
    }

    @Override
    public List<ItemEventoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO) throws Exception {
        return List.of();
    }

    private boolean existeEvento(LocalDateTime fechaEvento, String nombre, String ciudad) {

        return eventoRepo.buscarEvento(nombre, fechaEvento, ciudad).isPresent();

    }
}
