package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoControlador {

    @Autowired
    private final CarritoServicio carritoServicio;


    public CarritoControlador(CarritoServicio carritoServicio) {
        this.carritoServicio = carritoServicio;
    }

    @GetMapping("/listarCarrito")
    public ResponseEntity<List<Carrito>> obtenerCarrito() {
        List<Carrito> carrito = carritoServicio.findAll();
        return ResponseEntity.ok(carrito);
    }



    @PostMapping("/agregarItem/{id}")
    public ResponseEntity<String> agregarItem(@RequestBody DetalleCarrito item) throws Exception {
        carritoServicio.agregarItem("ID_CARRITO", item);
        return ResponseEntity.ok("Item agregado correctamente");
    }

    @DeleteMapping("/eliminarItem/{id}")
    public ResponseEntity<String> eliminarItem(@PathVariable String id) throws Exception {
        carritoServicio.eliminarItem(id);
        return ResponseEntity.ok("Item eliminado correctamente");
    }

    @GetMapping("/obtenerTablaCarrito")
    public ResponseEntity<String> obtenerTablaCarrito(@RequestParam List<DetalleCarrito> listaCarrito) throws Exception {
        return ResponseEntity.ok(carritoServicio.tablaCarrito(listaCarrito));
    }
}