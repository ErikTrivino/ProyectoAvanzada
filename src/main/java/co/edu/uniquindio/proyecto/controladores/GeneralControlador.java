package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.*;
import co.edu.uniquindio.proyecto.modelo.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.FiltroEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.proyecto.modelo.enums.Ciudad;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.servicios.implementaciones.OrdenServicioImpl;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.EventoServicio;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general")
public class GeneralControlador {

    private final EventoServicio eventoServicio;
    private final CuentaServicio cuentaServicio;

    private final OrdenServicioImpl ordenServicio;




    @GetMapping("/obtener-preferenciasUsuario/{idUsuario}")
    public List<ItemEventoDTO> obtenerPreferenciasUsario(@PathVariable String idUsuario) throws Exception {
        return cuentaServicio.obtenerPreferenciasUsuario(idUsuario); // null ya que no se usa idUsuario en este método
    }

    @PostMapping("/notificacion-pago")
    public void recibirNotificacionMercadoPago(@RequestBody Map<String, Object> requestBody) {
        ordenServicio.recibirNotificacionMercadoPago(requestBody);
    }
    @GetMapping("/obtener-info-evento/{id}")
    public ResponseEntity<MensajeDTO<InformacionEventoDTO>> obtenerInformacionEvento(@PathVariable String id) throws Exception{
        InformacionEventoDTO info = eventoServicio.obtenerInformacionEvento(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, info));
    }

    @GetMapping("/listar-todos-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventos() throws Exception {
        List<ItemEventoDTO> lista = eventoServicio.listarEventos();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }
    @GetMapping("/listar-all-evento")
    public ResponseEntity<MensajeDTO<List<InformacionEventoDTO>>> listarTodosEventos() throws Exception {
        List<InformacionEventoDTO> lista = eventoServicio.listarEventosAdmin();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }

    @GetMapping("/listar-tipo-eventos")
    public ResponseEntity<MensajeDTO<List<TipoEvento>>> listarTipoEventos() throws Exception {
        List<TipoEvento> lista = eventoServicio.obtenerTipoEventos();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }
    @GetMapping("/listar-ciudad-eventos")
    public ResponseEntity<MensajeDTO<List<Ciudad>>> listarCiudadesEventos() throws Exception {
        List<Ciudad> lista = List.of(Ciudad.values());
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }
    @GetMapping("/listar-eventos-activos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventosActivos() throws Exception {
        List<ItemEventoDTO> lista = eventoServicio.listarEventosActivos();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }
    @GetMapping("/listar-eventos-futuros")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventosFuturos() throws Exception {
        List<ItemEventoDTO> lista = eventoServicio.filtrarEventosFuturos();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }

    @PostMapping("/filtrar-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> filtrarEventos(@RequestBody FiltroEventoDTO filtroEventoDTO) throws Exception {
        List<ItemEventoDTO> lista = eventoServicio.filtrarEventos(filtroEventoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }

    @GetMapping("/obtener-evento/{id}")
    public ResponseEntity<MensajeDTO<Evento>> obtenerEvento(@PathVariable String id) throws Exception {
        Evento evento = eventoServicio.obtenerEvento(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, evento));
    }

    @PutMapping("/editar-perfil")
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@Valid @RequestBody EditarCuentaDTO cuenta) throws Exception{
        cuentaServicio.editarCuenta(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
    }

    @PutMapping("/eliminar-cuenta/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarCuenta(@PathVariable String id) throws Exception{
        cuentaServicio.eliminarCuenta(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }

    @GetMapping("/obtener-info-cuenta/{id}")
    public ResponseEntity<MensajeDTO<InformacionCuentaDTO>> obtenerInformacionCuenta(@PathVariable String id) throws Exception{
        InformacionCuentaDTO info = cuentaServicio.obtenerInformacionCuenta(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, info));
    }

    @GetMapping("/listar-todo-cuentas")
    public ResponseEntity<MensajeDTO<List<ItemCuentaDTO>>> listarCuentas() throws Exception {
        List<ItemCuentaDTO> lista = cuentaServicio.listarCuentas();
        return ResponseEntity.ok(new MensajeDTO<>(false, lista));
    }

    @GetMapping("/enviar-codigo-password/{correo}")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacionPassword(@PathVariable String correo) throws Exception {
        cuentaServicio.enviarCodigoRecuperacionPassword(correo);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Se ha enviado un correo con el código de recuperación de contraseña"));
    }

    @GetMapping("/enviar-codigo-auth/{correo}")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoActivacionCuenta(@PathVariable String correo) throws Exception {
        cuentaServicio.enviarCodigoActivacionCuenta(correo);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Se ha enviado un correo con el código de activación de su cuenta"));
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword(@RequestBody CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
        cuentaServicio.cambiarPassword(cambiarPasswordDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Su contraseña ha sido cambiada."));
    }

    @PutMapping("/activar-cuenta")
    public ResponseEntity<MensajeDTO<String>> activarCuenta(@RequestBody ActivarCuentaDTO activarCuentaDTO) throws Exception {
        cuentaServicio.activarCuenta(activarCuentaDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta activada exitosamente."));
    }


}
