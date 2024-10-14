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
    public String eliminarItem(String idCarrito, String idDetalleCarrito) throws Exception {
        //System.out.println(idCarrito +" "+idDetalleCarrito);
        Optional<Carrito> carrito = carritoRepo.findById(idCarrito);
        System.out.println(carrito.get().getItems().toString());
        if(carrito.isPresent()){
            Optional<DetalleCarrito> detalleCarrito = carrito.get().getItems().stream().filter(x -> x.getIdDetalleCarrito().equals(idDetalleCarrito) ).findFirst();
            if(detalleCarrito.isPresent()){
                carrito.get().getItems().remove(detalleCarrito.get());
                //
                carritoRepo.save(carrito.get());

                return "Item eliminado correctamente";
            }

        }


        return "Item no ha sido eliminado correctamente";
    }

    @Override
    public void agregarItem(String idCarrito, DetalleCarrito item) throws Exception {
        //System.out.println(idCarrito);
       Optional<Carrito> carrito = carritoRepo.findById(idCarrito);



        if(carrito.isPresent()){
            // Agregar el item al carrito
            carrito.get().getItems().add(item);

            // Guardar el carrito actualizado en la base de datos
            carritoRepo.save(carrito.get());
        }

    }

    @Override
    public Carrito traerCarrito(String idCarrito) throws Exception {
        Optional<Carrito> carrito = carritoRepo.findById(idCarrito);
//        System.out.println( carritoRepo.findById(idCuenta).get().getIdUsuario());
//        return carritoRepo.findById(idCuenta).get();
        if(carrito.isPresent()){
            return carrito.get();
        }else {
            return null;
        }

    }


}
