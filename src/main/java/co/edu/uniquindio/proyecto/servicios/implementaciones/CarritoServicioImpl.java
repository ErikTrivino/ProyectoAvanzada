package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoServicioImpl implements CarritoServicio {




    @Autowired
    private final CarritoRepo carritoRepo;

    @Override
    public String tablaCarrito(List<DetalleCarrito> listaCarrito) {

        return listaCarrito.toString(); // O un formato adecuado.
    }

    @Override
    public String crearCarrito(String idUsuario) throws Exception {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo o vacío.");
        }

        Carrito carrito = new Carrito();
        carrito.setIdUsuario(new ObjectId(idUsuario));
        carrito.setFecha(LocalDateTime.now());
        List<DetalleCarrito> detalles = new ArrayList<>();
        carrito.setItems(detalles);

        try {
            carritoRepo.save(carrito);
        } catch (Exception e) {
            throw new Exception("Error al crear el carrito: " + e.getMessage());
        }

        return carrito.getIdCarrito(); // O devuelve el carrito como DTO si es necesario
    }


    @Override
    public String eliminarItem(String idCarrito, String idEvento) throws Exception {
        Carrito carrito = carritoRepo.findById(idCarrito).orElseThrow(() -> new Exception("Carrito no encontrado"));
        carritoRepo.delete(carrito);
        return "Item eliminado correctamente";
    }

    @Override
    public void agregarItem(String idCarrito, DetalleCarrito item) throws Exception {
        Carrito carrito = carritoRepo.findById(idCarrito)
                .orElseThrow(() -> new Exception("Carrito no encontrado"));

        // Agregar el item al carrito
        carrito.getItems().add(item);

        // Guardar el carrito actualizado en la base de datos
        carritoRepo.save(carrito);
    }



    @Override
    public float calcularTotal(String idCarrito) throws Exception {
        return 0;
    }


    @Override
    public List<Carrito> listarCarritos() {
        return carritoRepo.findAll();
    }




}
