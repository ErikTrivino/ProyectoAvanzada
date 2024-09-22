package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import co.edu.uniquindio.proyecto.modelo.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.InformacionOrdenDTO;
import co.edu.uniquindio.proyecto.repositorios.OrdenRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.OrdenServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenServicioImpl implements OrdenServicio {
    private final OrdenRepo ordenRepo;

    @Override
    public String crearOrden(CrearOrdenDTO crearOrdenDTO) throws Exception {
        Orden nuevaOrden = new Orden();
        nuevaOrden.setIdCliente(crearOrdenDTO.idCliente());
        nuevaOrden.setFecha(LocalDateTime.now());
        nuevaOrden.setItems(crearOrdenDTO.items());
        nuevaOrden.setTotal(crearOrdenDTO.total());

        ordenRepo.save(nuevaOrden);
        return "La orden ha sido creada con éxito";
    }

    @Override
    public String actualizarOrden(EditarOrdenDTO editarOrdenDTO) throws Exception {
        Orden orden = obtenerOrden(editarOrdenDTO.id());

        orden.setItems(editarOrdenDTO.items());
        orden.setTotal(editarOrdenDTO.total());

        ordenRepo.save(orden);
        return "La orden ha sido actualizada con éxito";
    }

    @Override
    public String eliminarOrden(String idOrden) throws Exception {
        Orden orden = obtenerOrden(idOrden);
        ordenRepo.delete(orden);
        return "La orden ha sido eliminada.";
    }

    @Override
    public List<Orden> buscarOrdenesPorCliente(String idCliente) throws Exception {
        return ordenRepo.buscarOrdenesPorCliente(idCliente);
    }

    @Override
    public List<Orden> buscarOrdenesPorRangoDeFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws Exception {
        return ordenRepo.buscarOrdenesPorRangoDeFechas(fechaInicio, fechaFin);
    }

    @Override
    public InformacionOrdenDTO obtenerInformacionOrden(String idOrden) throws Exception {
        Orden orden = obtenerOrden(idOrden);
        return new InformacionOrdenDTO(
                orden.getId(),
                orden.getIdCliente(),
                orden.getFecha(),
                orden.getTotal(),
                orden.getItems()


        );
    }

    @Override
    public List<InformacionOrdenDTO> listarTodasLasOrdenes() throws Exception {
        List<Orden> ordenes = ordenRepo.findAll();
        return ordenes.stream()
                .map(orden -> new InformacionOrdenDTO(
                        orden.getId(),
                        orden.getIdCliente(),
                        orden.getFecha(),
                        orden.getTotal(),
                        orden.getItems()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InformacionOrdenDTO> listarOrdenesPorCliente(String idCliente) throws Exception {
        List<Orden> ordenes = ordenRepo.buscarOrdenesPorCliente(idCliente);
        return ordenes.stream()
                .map(orden -> new InformacionOrdenDTO(
                        orden.getId(),
                        orden.getIdCliente(),
                        orden.getFecha(),
                        orden.getTotal(),
                        orden.getItems()))
                .collect(Collectors.toList());
    }

    private Orden obtenerOrden(String idOrden) throws Exception {
        Optional<Orden> ordenOptional = ordenRepo.findById(idOrden);
        if (ordenOptional.isEmpty()) {
            throw new Exception("No se encontró una orden con el ID " + idOrden);
        }
        return ordenOptional.get();
    }
}
