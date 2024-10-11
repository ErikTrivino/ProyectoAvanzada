package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.ActivarCuentaDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.InformacionOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.servicios.implementaciones.CuponServicioImpl;
import co.edu.uniquindio.proyecto.servicios.implementaciones.OrdenServicioImpl;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cliente")
public class ClienteControlador {

    private final CuentaServicio cuentaServicio;
    private  final CuponServicioImpl cuponServicio;

    private final CarritoServicio carritoServicio;


    //Orden
    private final OrdenServicioImpl ordenServicio;



    @PostMapping("/realizar-pago")
    public ResponseEntity<MensajeDTO<Preference>> realizarPago(@RequestParam("idOrden") String idOrden) throws Exception{
        return ResponseEntity.ok().body(new MensajeDTO<>(false, ordenServicio.realizarPago(idOrden)));
    }

    @PostMapping("/notificacion-pago")
    public void recibirNotificacionMercadoPago(@RequestBody Map<String, Object> requestBody) {
        ordenServicio.recibirNotificacionMercadoPago(requestBody);
    }

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

    @GetMapping("/obtener-informacion-orden/{id}")
    public ResponseEntity<MensajeDTO<InformacionOrdenDTO>> obtenerInformacionOrden(@PathVariable String id) throws Exception {
        InformacionOrdenDTO cuponInfo = ordenServicio.obtenerInformacionOrden(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuponInfo));
    }
    @GetMapping("/obtener-ordenes-cliente-orden/{id}")
    public ResponseEntity<MensajeDTO<List<Orden>>> buscarOrdenesPorCliente(@PathVariable String id) throws Exception {
        List<Orden> ordenesCliente = ordenServicio.buscarOrdenesPorCliente(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesCliente));
    }
    @GetMapping("/obtener-ordenes-rango-fecha-orden/{dateOne}/{dateTwo}")
    public ResponseEntity<MensajeDTO<List<Orden>>> buscarOrdenesPorRangoDeFechas(@PathVariable("dateOne")String d1, @PathVariable("dateTwo")String d2) throws Exception {
        List<Orden> ordenesClientes = ordenServicio.buscarOrdenesPorRangoDeFechas(d1,d2);
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesClientes));
    }
    @GetMapping("/obtener-ordenes-orden")
    public ResponseEntity<MensajeDTO<List<InformacionOrdenDTO>>> buscarOrdenes() throws Exception {
        List<InformacionOrdenDTO> ordenesCliente = ordenServicio.listarTodasLasOrdenes();
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenesCliente));
    }
    ///Preferencias
    // Obtener todas las preferencias
    @GetMapping("/obtener-preferencias")
    public List<TipoEvento> obtenerPreferencias() throws Exception {
        return cuentaServicio.obtenerPreferencias(); // null ya que no se usa idUsuario en este método
    }

    // Agregar preferencias a una cuenta de usuario
    @PostMapping("/agregarPreferenciasUsuario-preferencias/{idUsuario}")
    public String agregarPreferenciasUsuario(
            @PathVariable String idUsuario,
            @RequestBody List<TipoEvento> tipoPreferencias) throws Exception {
        cuentaServicio.agregarPreferenciasUsuario(idUsuario, tipoPreferencias);
        return "Preferencias agregadas al usuario con éxito.";
    }
    //Boleta

    // RF-001: Buscar boletas por nombre o identificación
    @GetMapping("/buscar-boleta")
    public ResponseEntity<List<Boleta>> buscarBoletasPorNombreOIdentificacion(
            @RequestParam("nombre") String nombreOId) throws Exception {
        List<Boleta> boletas = cuentaServicio.buscarBoletasPorPropietario(nombreOId);
        return ResponseEntity.ok(boletas);
    }

    // RF-002: Mostrar todas las boletas en propiedad del usuario
    @GetMapping("/listarBoletasPropietario-boleta/{idPropietario}")
    public ResponseEntity<List<Boleta>> listarBoletasPorPropietario(
            @PathVariable String idPropietario) throws Exception {
        List<Boleta> boletas = cuentaServicio.buscarBoletasPorPropietario(idPropietario);
        return ResponseEntity.ok(boletas);
    }

    // RF-003: Ver detalle de una boleta
    @GetMapping("/detalle-boleta/{idBoleta}/{idPropietario}")
    public ResponseEntity<Boleta> obtenerDetalleBoleta(
            @PathVariable String idBoleta,
            @PathVariable String idPropietario) throws Exception {
        Boleta boleta = cuentaServicio.obtenerDetalleBoleta(idBoleta, idPropietario);
        return ResponseEntity.ok(boleta);
    }

    // RF-004: Ver boletas enviadas y pendientes
    @GetMapping("listarBoletasEnviadas-boleta/{idPropietario}/envios/enviados")
    public ResponseEntity<List<Boleta>> listarBoletasEnviadas(
            @PathVariable String idPropietario) throws Exception {
        List<Boleta> boletasEnviadas = cuentaServicio.listarBoletasEnviadas(idPropietario);
        return ResponseEntity.ok(boletasEnviadas);
    }

    @GetMapping("listarBoletasPendientes-boleta/{idPropietario}/envios/pendientes")
    public ResponseEntity<List<Boleta>> listarBoletasPendientes(
            @PathVariable String idPropietario) throws Exception {
        List<Boleta> boletasPendientes = cuentaServicio.listarBoletasPendientes(idPropietario);
        return ResponseEntity.ok(boletasPendientes);
    }

    // RF-009: Transferir boleta
    @PostMapping("/transferir-boleta/{idBoleta}/{idPropietario}/{idNuevoPropietario}")
    public ResponseEntity<String> transferirBoleta(
            @PathVariable String idBoleta,
            @PathVariable String idPropietario,
            @PathVariable String idNuevoPropietario) throws Exception {
        cuentaServicio.transferirBoleta(idBoleta, idPropietario, idNuevoPropietario);
        return ResponseEntity.ok("Boleta transferida exitosamente.");
    }

    // RF-008: Aceptar boleta
    @PostMapping("/aceptar-boleta/{idBoleta}/{idNuevoPropietario}")
    public ResponseEntity<String> aceptarBoleta(
            @PathVariable String idBoleta,
            @PathVariable String idNuevoPropietario) throws Exception {
        cuentaServicio.aceptarBoleta(idBoleta, idNuevoPropietario);
        return ResponseEntity.ok("Boleta aceptada.");
    }

    //Carrito
    @PostMapping("/agregarItem-carrito/{id}")
    public ResponseEntity<MensajeDTO<String>> agregarItem(@RequestBody DetalleCarrito item) throws Exception {
        carritoServicio.agregarItem("ID_CARRITO", item);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Item agregado correctamente"));
    }

    @DeleteMapping("/eliminarItem-carrito/{id}/{idEvento}")
    public ResponseEntity<MensajeDTO<String>> eliminarItem(@PathVariable String id, @PathVariable String idEvento) throws Exception {
        carritoServicio.eliminarItem(id, idEvento);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Item eliminado correctamente"));
    }
    @GetMapping("/traerCarrito-carrito/{id}")
    public ResponseEntity<MensajeDTO<Carrito>>  traerCArrito(@PathVariable String id) throws Exception {
        return  ResponseEntity.ok(new MensajeDTO<>(false,carritoServicio.traerCarrito(id)));
    }

    @PutMapping("/activar-cuenta")
    public ResponseEntity<MensajeDTO<String>> activarCuenta(@RequestBody ActivarCuentaDTO activarCuentaDTO) throws Exception {
        cuentaServicio.activarCuenta(activarCuentaDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta activada exitosamente."));
    }
    //Cupon
    @PostMapping("/redimir-cupon/{codigo}")
    public ResponseEntity<MensajeDTO<String>> redimirCupon(@PathVariable String codigo) throws Exception {
        boolean resultado = cuponServicio.redimirCupon(codigo);
        return resultado
                ? ResponseEntity.ok(new MensajeDTO<>(false, "Cupon redimido exitosamente"))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensajeDTO<>(true, "El cupon no pudo ser redimido"));
    }

}
