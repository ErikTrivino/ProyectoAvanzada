package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
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
            Carrito carritoActual = carrito.get();

            // Verificar si el item ya está en el carrito
            Optional<DetalleCarrito> itemExistente = carritoActual.getItems().stream()
                    .filter(i -> i.getIdEvento().equals(item.getIdEvento()))
                    .findFirst();

            if (itemExistente.isPresent()) {
                // Si el item ya existe, incrementar la cantidad
                DetalleCarrito detalleExistente = itemExistente.get();
                detalleExistente.setCantidad(detalleExistente.getCantidad() + item.getCantidad());
            } else {
                // Si el item no existe, agregarlo al carrito
                carritoActual.getItems().add(item);
            }
            // Guardar el carrito actualizado en la base de datos
            carritoRepo.save(carrito.get());
        }

    }

    @Override
    public void agregarItemUnico(String idCuenta, DetalleCarrito item) throws Exception {
        //System.out.println(idCarrito);
        Optional<Carrito> carrito = carritoRepo.buscarCarritoPorIdUsuario(idCuenta);



        if(carrito.isPresent()){
            Carrito carritoActual = carrito.get();

            // Verificar si el item ya está en el carrito
            Optional<DetalleCarrito> itemExistente = carritoActual.getItems().stream()
                    .filter(i -> i.getIdEvento().equals(item.getIdEvento()))
                    .findFirst();

            if (itemExistente.isPresent()) {
                // Si el item ya existe, incrementar la cantidad
                DetalleCarrito detalleExistente = itemExistente.get();
                detalleExistente.setCantidad(detalleExistente.getCantidad() + item.getCantidad());
            } else {
                // Si el item no existe, agregarlo al carrito
                carritoActual.getItems().add(item);
            }
            // Guardar el carrito actualizado en la base de datos
            carritoRepo.save(carrito.get());
        }
    }

    @Override
    public void editarItem(String idCarrito, DetalleCarrito item) throws Exception {
        Optional<Carrito> carrito = carritoRepo.findById(idCarrito);

        if (carrito.isPresent()) {
            Carrito carritoActual = carrito.get();

            // Buscar el item que se desea editar en el carrito
            Optional<DetalleCarrito> itemExistente = carritoActual.getItems().stream()
                    .filter(i -> i.getIdEvento().equals(item.getIdEvento()))
                    .findFirst();

            if (itemExistente.isPresent()) {
                // Si el item existe, actualizar los detalles
                DetalleCarrito detalleExistente = itemExistente.get();
                detalleExistente.setCantidad(item.getCantidad());
                detalleExistente.setNombreLocalidad(item.getNombreLocalidad());
                // Actualizar otros campos de detalleExistente según sea necesario

                // Guardar el carrito actualizado en la base de datos
                carritoRepo.save(carritoActual);
            } else {
                throw new Exception("Item no encontrado en el carrito");
            }
        } else {
            throw new Exception("Carrito no encontrado");
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
            throw  new Exception("No se ha encontrado un carrito");
        }

    }

    public Carrito traerCarritoCliente(String idCuenta) throws  Exception{
        Optional<Carrito> carrito = carritoRepo.buscarCarritoPorIdUsuario(idCuenta);
//        System.out.println( carritoRepo.findById(idCuenta).get().getIdUsuario());
//        return carritoRepo.findById(idCuenta).get();
        if(carrito.isPresent()){
            return carrito.get();
        }else {
            throw  new Exception("No se ha encontrado un carrito");
        }

    }

    @Override
    public void vaciarCarrito(String idCarrito) throws Exception {
        Optional<Carrito> carrito = carritoRepo.findById(idCarrito);

        if (carrito.isPresent()) {
            Carrito carritoActual = carrito.get();

            // Vaciar la lista de ítems del carrito
            carritoActual.getItems().clear();

            // Guardar el carrito actualizado en la base de datos
            carritoRepo.save(carritoActual);
        } else {
            throw new Exception("Carrito no encontrado");
        }
    }



}
