package co.edu.uniquindio.proyecto.controladoresNoutil;
import co.edu.uniquindio.proyecto.modelo.documentos.Cupon;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.proyecto.servicios.implementaciones.CuponServicioImpl;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuponServicio;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cliente")
//@SecurityRequirement(name = "bearerAuth")
public class CuponControlador {
    private  final CuponServicioImpl cuponServicio;

    @PostMapping("/crear-cupon")
    public ResponseEntity<MensajeDTO<String>> crearCupon(@Valid @RequestBody CrearCuponDTO cupon) throws Exception {
        cuponServicio.crearCupon(cupon);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta creada exitosamente"));

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

    @GetMapping("/obtener-informacion-cupon/{id}")
    public ResponseEntity<MensajeDTO<InformacionCuponDTO>> obtenerInformacionCupon(@PathVariable String id) throws Exception {
        InformacionCuponDTO cuponInfo = cuponServicio.obtenerInformacionCupon(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuponInfo));
    }

    @GetMapping("/listar-cupones-activos-cupon")
    public ResponseEntity<MensajeDTO<List<Cupon>>> listarCuponesActivos() throws Exception {
        List<Cupon> cupones = cuponServicio.listarCuponesActivos();
        return ResponseEntity.ok(new MensajeDTO<>(false, cupones));
    }

    @PostMapping("/redimir-cupon/{codigo}")
    public ResponseEntity<MensajeDTO<String>> redimirCupon(@PathVariable String codigo) throws Exception {
        boolean resultado = cuponServicio.redimirCupon(codigo);
        return resultado
                ? ResponseEntity.ok(new MensajeDTO<>(false, "Cupon redimido exitosamente"))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensajeDTO<>(true, "El cupon no pudo ser redimido"));
    }

    @GetMapping("/listar-cupones-por-expirar-cupon")
    public ResponseEntity<MensajeDTO<List<Cupon>>> listarCuponesPorExpirar() throws Exception {
        List<Cupon> cupones = cuponServicio.listarCuponesPorExpirar();
        return ResponseEntity.ok(new MensajeDTO<>(false, cupones));
    }
}
