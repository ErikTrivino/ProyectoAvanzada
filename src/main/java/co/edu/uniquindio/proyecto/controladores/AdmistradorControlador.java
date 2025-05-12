package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.documentos.Cupon;
import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.EditarEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.InformacionOrdenDTO;
import co.edu.uniquindio.proyecto.servicios.implementaciones.CuponServicioImpl;
import co.edu.uniquindio.proyecto.servicios.implementaciones.OrdenServicioImpl;
import co.edu.uniquindio.proyecto.servicios.interfaces.EventoServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.ImagenesServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdmistradorControlador {

    private final EventoServicio eventoServicio;
    private final ImagenesServicio imagenesServicio;

    private  final CuponServicioImpl cuponServicio;

    private final OrdenServicioImpl ordenServicio;

    //Orden
    @GetMapping("/obtener-ordenes-rango-fecha-orden/{dateOne}/{dateTwo}")
    public ResponseEntity<MensajeDTO<List<Orden>>> buscarOrdenesPorRangoDeFechas(@PathVariable("dateOne")String d1, @PathVariable("dateTwo")String d2) throws Exception {
        List<Orden> ordenesClientes = ordenServicio.buscarOrdenesPorRangoDeFechas(d1,d2);
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesClientes));
    }
    @GetMapping("/obtener-evento/{id}")
    public ResponseEntity<MensajeDTO<Evento>> obtenerEvento(@PathVariable String id) throws Exception {
        Evento evento = eventoServicio.obtenerEvento(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, evento));
    }



    @GetMapping("/obtener-ordenes-orden")
    public ResponseEntity<MensajeDTO<List<InformacionOrdenDTO>>> buscarOrdenes() throws Exception {
        List<InformacionOrdenDTO> ordenesCliente = ordenServicio.listarTodasLasOrdenes();
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesCliente));
    }

    //Evento

    @GetMapping("/listar-todos-eventos-admin")
    public ResponseEntity<MensajeDTO<List<InformacionEventoDTO>>> listarEventos() throws Exception {
        List<InformacionEventoDTO> lista = eventoServicio.listarEventosAdmin();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }
    @PostMapping("/crear-evento")
    public ResponseEntity<MensajeDTO<String>> crearEvento(@Valid @RequestBody CrearEventoDTO evento) throws Exception{
        eventoServicio.crearEvento(evento);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Evento creado exitosamente"));
    }

    @PutMapping("/editar-evento")
    public ResponseEntity<MensajeDTO<String>> editarEvento(@Valid @RequestBody EditarEventoDTO evento) throws Exception{
        eventoServicio.editarEvento(evento);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Evento editado exitosamente"));
    }

    @DeleteMapping("/eliminar-evento/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarEvento(@PathVariable String id) throws Exception{
        eventoServicio.eliminarEvento(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Evento eliminado exitosamente"));
    }

    @PostMapping("/subir")
    public ResponseEntity<MensajeDTO<Map>> subir(@RequestParam("imagen") MultipartFile imagen) throws Exception{
        Map respuesta = imagenesServicio.subirImagen(imagen);
        return ResponseEntity.ok().body(new MensajeDTO<Map>(false, respuesta));
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<MensajeDTO<String>> eliminar(@RequestParam("idImagen") String idImagen)  throws Exception{
        imagenesServicio.eliminarImagen( idImagen );
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "La imagen fue eliminada correctamente"));
    }

    //Cupon
    @PostMapping("/crear-cupon")
    public ResponseEntity<MensajeDTO<String>> crearCupon(@Valid @RequestBody CrearCuponDTO cupon) throws Exception {
        cuponServicio.crearCupon(cupon);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupon creado exitosamente"));

    }
    @PutMapping("/actualizar-cupon")
    public ResponseEntity<MensajeDTO<String>> actualizarCupon(@Valid @RequestBody EditarCuponDTO cupon) throws Exception {
        cuponServicio.actualizarCupon(cupon);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupon actualizado exitosamente"));
    }

    @DeleteMapping("/eliminar-cupon/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarCupon(@PathVariable String id) throws Exception {
        cuponServicio.eliminarCupon(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupon eliminado exitosamente"));
    }
    @GetMapping("/listar-cupones-por-expirar-cupon")
    public ResponseEntity<MensajeDTO<List<Cupon>>> listarCuponesPorExpirar() throws Exception {
        List<Cupon> cupones = cuponServicio.listarCuponesPorExpirar();
        return ResponseEntity.ok(new MensajeDTO<>(false, cupones));
    }

    @GetMapping("/listar-cupones-activos-cupon")
    public ResponseEntity<MensajeDTO<List<Cupon>>> listarCuponesActivos() throws Exception {
        List<Cupon> cupones = cuponServicio.listarCuponesActivos();
        return ResponseEntity.ok(new MensajeDTO<>(false, cupones));
    }
    @GetMapping("/listar-cupones")
    public ResponseEntity<MensajeDTO<List<Cupon>>> listarCupones() throws Exception {
        List<Cupon> cupones = cuponServicio.listarCupones();
        return ResponseEntity.ok(new MensajeDTO<>(false, cupones));
    }
    @GetMapping("/obtener-informacion-cupon/{id}")
    public ResponseEntity<MensajeDTO<InformacionCuponDTO>> obtenerInformacionCupon(@PathVariable String id) throws Exception {
        InformacionCuponDTO cuponInfo = cuponServicio.obtenerInformacionCupon(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuponInfo));
    }

}
