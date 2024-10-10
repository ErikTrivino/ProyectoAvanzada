package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import co.edu.uniquindio.proyecto.repositorios.CuentaRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoServicioImpl implements CarritoServicio {




    @Autowired
    private final CarritoRepo carritoRepo;

    private final CuentaRepo cuentaRepo;


    @Override
    public String eliminarItem(String idCarrito, String idEvento) throws Exception {
        Carrito carrito = carritoRepo.findById(idCarrito).orElseThrow(() -> new Exception("Carrito no encontrado"));
        Optional<DetalleCarrito> detalleCarrito = carrito.getItems().stream().filter(x -> x.getIdEvento() == idEvento).findFirst();
        if(detalleCarrito.isPresent()){
            carrito.getItems().remove(detalleCarrito.get());
            carritoRepo.save(carrito);
            return "Item eliminado correctamente";
        }
        return "Item no ha sido eliminado correctamente";

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
    public Carrito traerCarrito(String idCuenta) throws Exception {
        Optional<Carrito> carrito = carritoRepo.buscarCarritoPorIdUsuario(idCuenta);
        if(carrito.isPresent()){
            return carrito.get();
        }else {
            return null;
        }

    }


}
