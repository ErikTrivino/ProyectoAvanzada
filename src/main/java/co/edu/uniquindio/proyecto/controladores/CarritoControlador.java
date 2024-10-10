package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequiredArgsConstructor
@RequestMapping("/api/cliente")
public class CarritoControlador {

    @Autowired
    private final CarritoServicio carritoServicio;




    @PostMapping("/agregarItem-carrito/{id}")
    public ResponseEntity<MensajeDTO<String>> agregarItem(@RequestBody DetalleCarrito item) throws Exception {
        carritoServicio.agregarItem("ID_CARRITO", item);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Item agregado correctamente"));
    }

    @DeleteMapping("/eliminarItem-carrito/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarItem(@PathVariable String id) throws Exception {
        carritoServicio.eliminarItem(id, "ID_EVENTO");
        return ResponseEntity.ok(new MensajeDTO<>(false,"Item eliminado correctamente"));
    }
    @GetMapping("/traerCarrito-carrito/{id}")
    public Carrito traerCArrito(@PathVariable String id) throws Exception {
       return carritoServicio.traerCarrito(id);
    }


}