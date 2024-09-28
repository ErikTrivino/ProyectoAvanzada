package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleCarrito;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoServicioImpl implements CarritoServicio {

    @Autowired
    private final CarritoRepo carritoRepo;

    public CarritoServicioImpl(CarritoRepo carritoRepo) {
        this.carritoRepo = carritoRepo;
    }

    @Override
    public String tablaCarrito(List<DetalleCarrito> listaCarrito) {
        // Generar la tabla en formato texto o HTML
        // Dependiendo de la lógica, aquí puedes construir una tabla visual.
        return listaCarrito.toString(); // O un formato adecuado.
    }

    @Override
    public String eliminarItem(String idCarrito) throws Exception {
        Carrito carrito = carritoRepo.findById(idCarrito).orElseThrow(() -> new Exception("Carrito no encontrado"));
        carritoRepo.delete(carrito);
        return "Item eliminado correctamente";
    }

    @Override
    public void agregarItem(String idCarrito, DetalleCarrito item) throws Exception {
        Carrito carrito = carritoRepo.findById(idCarrito).orElseThrow(() -> new Exception("Carrito no encontrado"));
        carritoRepo.save(carrito);
    }


    @Override
    public float calcularTotal(String idCarrito) throws Exception {
        return 0;
    }


}
