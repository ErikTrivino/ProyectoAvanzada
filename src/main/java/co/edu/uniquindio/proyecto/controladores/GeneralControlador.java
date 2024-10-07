package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.EditarCuentaDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.InformacionCuentaDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.ItemCuentaDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.CrearEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.FiltroEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.InformacionEventoDTO;
import co.edu.uniquindio.proyecto.modelo.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.EventoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general")
public class GeneralControlador {

    private final EventoServicio eventoServicio;
    private final CuentaServicio cuentaServicio;

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

    @DeleteMapping("/eliminar-cuenta/{id}")
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
}
