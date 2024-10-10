package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cliente")
public class BoletaControlador {

    @Autowired
    private CuentaServicio cuentaServicio;

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
    @GetMapping("/detalle-boleta/{idBoleta}")
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
}
