package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.servicios.implementaciones.CuentaServicioImpl;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferencias")
public class PreferenciaControlador {

    @Autowired
    private CuentaServicioImpl cuentaServicio;


    // Obtener todas las preferencias
    @GetMapping("/obtener")
    public List<TipoEvento> obtenerPreferencias() throws Exception {
        return cuentaServicio.obtenerPreferencias(); // null ya que no se usa idUsuario en este método
    }

    // Agregar preferencias a una cuenta de usuario
    @PostMapping("/agregarPreferenciasUsuario/{idUsuario}")
    public String agregarPreferenciasUsuario(
            @PathVariable String idUsuario,
            @RequestBody List<TipoEvento> tipoPreferencias) throws Exception {
        cuentaServicio.agregarPreferenciasUsuario(idUsuario, tipoPreferencias);
        return "Preferencias agregadas al usuario con éxito.";
    }
}

