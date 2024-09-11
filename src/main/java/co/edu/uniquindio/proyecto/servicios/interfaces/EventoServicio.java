package co.edu.uniquindio.proyecto.servicios.interfaces;

import co.edu.uniquindio.proyecto.modelo.dto.evento.*;

import java.util.List;

public interface EventoServicio {

    String crearEvento(CrearEventoDTO crearEventoDTO) throws Exception;

    String editarEvento(EditarEventoDTO editarEventoDTO) throws Exception;;

    String eliminarEvento(String id) throws Exception;

    InformacionEventoDTO obtenerInformacionEvento(String id) throws Exception;

    List<ItemEventoDTO> listarEventos() throws Exception;

    List<ItemEventoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO) throws Exception;
}
