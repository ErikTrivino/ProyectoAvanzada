package co.edu.uniquindio.proyecto.controladoresMalos;

import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cuenta")
//@SecurityRequirement(name = "bearerAuth")
public class CuentaControlador {

    private final CuentaServicio cuentaServicio;


}
