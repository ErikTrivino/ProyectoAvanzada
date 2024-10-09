package co.edu.uniquindio.proyecto.controladores;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.MensajeDTO;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController

@RequiredArgsConstructor
@RequestMapping("/api/carrito")
public class CarritoControlador {

    @Autowired
    private final CarritoServicio carritoServicio;
    @Autowired
    private final CarritoRepo carritoRepo;


    @GetMapping("/listarCarrito")
    public ResponseEntity<List<Carrito>> obtenerCarrito() {
        List<Carrito> carrito = carritoServicio.listarCarritos();
        return ResponseEntity.ok(carrito);
    }
    @PostMapping("/crearCarrito/{idUsuario}")
    public ResponseEntity<MensajeDTO<String>> crearCarrito(@PathVariable ObjectId idUsuario) throws Exception {
        // Crear un carrito vacío
        Carrito carrito = new Carrito();
        carrito.setIdUsuario(idUsuario);
        carrito.setItems(new ArrayList<>());
        carrito.setFecha(LocalDateTime.now());

        // Guardar carrito en la base de datos
        Carrito carritoGuardado = carritoRepo.save(carrito);

        return ResponseEntity.ok(new MensajeDTO<>(false, "Carrito creado exitosamente"));
    }



    @PostMapping("/agregarItem/{idCarrito}")
    public ResponseEntity<MensajeDTO<String>> agregarItem(@PathVariable String idCarrito, @RequestBody DetalleCarrito item) throws Exception {
        carritoServicio.agregarItem(idCarrito, item); // Ahora se usa el idCarrito de la URL
        return ResponseEntity.ok(new MensajeDTO<>(false,"Item agregado correctamente"));


    }
    @PostMapping("/carrito/agregarItem/{idCarrito}")
    public ResponseEntity<MensajeDTO<String>> agregarItem(@RequestBody DetalleCarrito item) throws Exception {
        carritoServicio.agregarItem("idCarrito", item);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Item agregado correctamente"));
    }


    @DeleteMapping("/eliminarItem/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarItem(@PathVariable String id) throws Exception {
        carritoServicio.eliminarItem(id, "ID_EVENTO");
        return ResponseEntity.ok(new MensajeDTO<>(false,"Item eliminado correctamente"));
    }

    @GetMapping("/obtenerTablaCarrito")
    public ResponseEntity<MensajeDTO<String>> obtenerTablaCarrito(@RequestParam List<DetalleCarrito> listaCarrito) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false,carritoServicio.tablaCarrito(listaCarrito)));
    }
}