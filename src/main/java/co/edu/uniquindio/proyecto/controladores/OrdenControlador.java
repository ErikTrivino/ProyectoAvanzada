package co.edu.uniquindio.proyecto.controladores;
import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.InformacionOrdenDTO;
import co.edu.uniquindio.proyecto.servicios.implementaciones.OrdenServicioImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orden")
public class OrdenControlador {

    private final OrdenServicioImpl ordenServicio;

    @PostMapping("/crear-orden")
    public ResponseEntity<MensajeDTO<String>> crearOrden(@Valid @RequestBody CrearOrdenDTO orden) throws Exception {
        ordenServicio.crearOrden(orden);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Orden creada exitosamente"));

    }
    @PutMapping("/actualizar-orden")
    public ResponseEntity<MensajeDTO<String>> actualizarOrden(@Valid @RequestBody EditarOrdenDTO orden) throws Exception {
        ordenServicio.actualizarOrden(orden);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Orden actualizado exitosamente"));
    }

    @DeleteMapping("/eliminar-orden/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarCupon(@PathVariable String id) throws Exception {
        ordenServicio.eliminarOrden(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Orden eliminado exitosamente"));
    }

    @GetMapping("/obtener-informacion/{id}")
    public ResponseEntity<MensajeDTO<InformacionOrdenDTO>> obtenerInformacionOrden(@PathVariable String id) throws Exception {
        InformacionOrdenDTO cuponInfo = ordenServicio.obtenerInformacionOrden(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuponInfo));
    }
    @GetMapping("/obtener-ordenes-cliente/{id}")
    public ResponseEntity<MensajeDTO<List<Orden>>> buscarOrdenesPorCliente(@PathVariable String id) throws Exception {
        List<Orden> ordenesCliente = ordenServicio.buscarOrdenesPorCliente(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesCliente));
    }
    @GetMapping("/obtener-ordenes-rango-fecha/{dateOne}/{dateTwo}")
    public ResponseEntity<MensajeDTO<List<Orden>>> buscarOrdenesPorRangoDeFechas(@PathVariable("dateOne")String d1, @PathVariable("dateTwo")String d2) throws Exception {
        List<Orden> ordenesClientes = ordenServicio.buscarOrdenesPorRangoDeFechas(d1,d2);
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesClientes));
    }


    @GetMapping("/obtener-ordenes")
    public ResponseEntity<MensajeDTO<List<InformacionOrdenDTO>>> buscarOrdenes() throws Exception {
        List<InformacionOrdenDTO> ordenesCliente = ordenServicio.listarTodasLasOrdenes();
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesCliente));
    }

}
